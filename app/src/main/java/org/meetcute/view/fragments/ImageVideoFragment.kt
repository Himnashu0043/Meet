package org.meetcute.view.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import org.meetcute.R
import org.meetcute.databinding.CardLoopImageBinding
import org.meetcute.databinding.FragmentImageVideoBinding
import org.meetcute.databinding.ItemVideoBinding
import org.meetcute.appUtils.BaseAdapter

class ImageVideoFragment : BaseFragment<FragmentImageVideoBinding>() {

    private var position:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
    }
    private val imageAdapter: ImageAdapter by lazy {
        ImageAdapter()
    }
    private val videoAdapter: VideoAdapter by lazy {
        VideoAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(position != 0) {
            binding.rvVideos.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvVideos.adapter = imageAdapter
            imageAdapter.set(pref.userMedia?.Post?.map { it.fileUrl }?: emptyList())
        }else{
            binding.rvVideos.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvVideos.adapter = videoAdapter
            videoAdapter.add(pref.userMedia?.Thumbnail_Video?.firstOrNull()?.fileUrl?:"")
            videoAdapter.add(pref.userMedia?.Self_Loop_Video?.firstOrNull()?.fileUrl?:"")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_image_video
    }

    inner class ImageAdapter : BaseAdapter<String>() {

        inner class ImageVideoViewHolder(val binding: CardLoopImageBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ImageVideoViewHolder(
                CardLoopImageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as ImageVideoViewHolder
            holder.binding.url = get(position)
        }
    }

    class VideoAdapter :
        BaseAdapter<String>() {

        private var player: SimpleExoPlayer? = null

        inner class VideoViewHolder(val binding:ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {

            init {
                player = SimpleExoPlayer.Builder(itemView.context).build()
                player?.volume = 0f
                player?.repeatMode = SimpleExoPlayer.REPEAT_MODE_ALL
                binding.player.player = player
            }

            fun bind(uri: Uri) {
                player?.addListener(object : Player.Listener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        super.onPlayerStateChanged(playWhenReady, playbackState)
                        if (playbackState == Player.STATE_READY) {
                            // Hide loading indicator when video is ready
                            binding.loadingIndicator.visibility = View.GONE
                        }else binding.loadingIndicator.visibility = View.VISIBLE
                    }
                })
                player?.setMediaItem(MediaItem.fromUri(uri))
                player?.prepare()
                player?.playWhenReady = true
            }

            fun releasePlayer() {
                player?.release()
                player = null
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
            return VideoViewHolder(
                ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as VideoViewHolder
            val videoUri = Uri.parse(get(position))
            holder.bind(videoUri)
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            holder as VideoViewHolder
            holder.releasePlayer()
        }

    }


}