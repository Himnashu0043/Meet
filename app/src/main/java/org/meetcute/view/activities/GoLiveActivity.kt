package org.meetcute.view.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.FragmentMyPreviewBinding
import org.meetcute.databinding.ItemLoopVideoBinding
import org.meetcute.databinding.ItemVideoBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.view.fragments.ImageVideoFragment
import org.meetcute.view.fragments.bottomSheets.LiveVideoSettingBottomSheet
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.SupportViewModel

@AndroidEntryPoint
class GoLiveActivity : BaseActivity<FragmentMyPreviewBinding>(), SurfaceHolder.Callback {
    private val viewModel: SupportViewModel by viewModels()
    private var getCanJoinAnyFollow: String? = "Anyone"
    private var getCanChatAnyfollow: String? = "Anyone"
    private var getmic: Boolean = false
    private var getsound: Boolean = false
    private var player: SimpleExoPlayer? = null
    override fun getLayout(): Int {
        return R.layout.fragment_my_preview
    }

    private lateinit var surfaceHolder: SurfaceHolder
    private var camera: Camera? = null
    private var CAMERA_PERMISSION_REQUEST = 111


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Glide.with(binding.ivBg).asGif().load(R.raw.loop_video_gif)
//            .centerCrop()
//            .into(binding.ivBg)
        binding.user = pref.user
        surfaceHolder = binding.surfaceView.holder
        surfaceHolder.addCallback(this)
        player = SimpleExoPlayer.Builder(this).build()
        binding.player.player = player
        player?.volume = 1f
        player?.repeatMode = SimpleExoPlayer.REPEAT_MODE_ALL
        player?.setMediaItem(
            MediaItem.fromUri(
                pref.userMedia?.Thumbnail_Video?.firstOrNull()?.fileUrl ?: ""
            )
        )
        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {
                    binding.loadingIndicator.visibility = View.GONE

                } else {
                    binding.loadingIndicator.visibility = View.VISIBLE
                }

            }
        })
        println("================uir${pref.userMedia?.Thumbnail_Video?.firstOrNull()?.fileUrl ?: ""}")
        player?.prepare()
        player?.playWhenReady = true
        player?.play()

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivSetting.setOnClickListener {
            LiveVideoSettingBottomSheet { canJoinAnyFollow, canChatAnyFollow, mic, sound ->
                getCanJoinAnyFollow = canJoinAnyFollow
                getCanChatAnyfollow = canChatAnyFollow
                getmic = mic
                getsound = sound
                pref.getmic = getmic
                pref.getsound = getsound
                pref.getCanChatAnyfollow = getCanChatAnyfollow.toString()
                pref.getCanJoinAnyFollow = getCanJoinAnyFollow.toString()

                println("==========${getCanJoinAnyFollow},${getCanChatAnyfollow},${getmic},${getsound}")
            }.show(supportFragmentManager, "")
        }
        binding.llGoLive.setOnClickListener {
            pref.getEditText = binding.etMessage.text.trim().toString()
            if (binding.etMessage.text.trim().toString().isNotEmpty()) {
                startActivity(Intent(this, LiveStreamingActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter the message!!", Toast.LENGTH_SHORT).show()
            }


        }
        viewModel.getRTMTokenResponse.observe(this) {
            when (it) {
                is NetworkResponse.Success -> {
                    if (it.value?.success == true) {
                        Log.d(
                            "BaseLiveStreamingActivi",
                            " rtm token got saved ${it.value?.data?.rtmToken ?: ""}"
                        )
                        pref.getRTMToken = it.value?.data?.rtmToken ?: ""
                    }
                }

                else -> {}
            }
        }

    }

    override fun onShowKeyboard(keyboardHeight: Int) {
        binding.tvGoLive.visibility = View.GONE
    }

    override fun onHideKeyboard() {
        binding.tvGoLive.visibility = View.VISIBLE
    }



    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = h.toDouble() / w
        if (sizes == null) return null
        var optimalSize: Camera.Size? = null
        var minDiff = Double.MAX_VALUE
        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(holder)
            val mSupportedPreviewSizes = camera?.getParameters()?.getSupportedPreviewSizes();
            if (mSupportedPreviewSizes != null) {
                val mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, binding.surfaceView.width, binding.surfaceView.height);
                val parameters: Camera.Parameters? = camera?.getParameters()
                parameters?.setPreviewSize(mPreviewSize!!.width, mPreviewSize!!.height)
                camera?.setParameters(parameters)
                camera?.startPreview()
            }else{
                camera?.startPreview()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                surfaceHolder.addCallback(this)
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRTMToken(pref.user?._id ?: "")
    }
    /* class VideoAdapter :
         BaseAdapter<String>() {

         private var player: SimpleExoPlayer? = null

         inner class VideoViewHolder(val binding: ItemLoopVideoBinding) :
             RecyclerView.ViewHolder(binding.root) {

             init {
                 player = SimpleExoPlayer.Builder(itemView.context).build()
                 player?.volume = 1f
                 player?.repeatMode = SimpleExoPlayer.REPEAT_MODE_ALL
                 binding.player.player = player
                 player?.play()
             }

             fun bind(uri: Uri) {
                 println("====================uri$uri")
                 player?.addListener(object : Player.Listener {
                     override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                         super.onPlayerStateChanged(playWhenReady, playbackState)
                         if (playbackState == Player.STATE_READY) {
                             // Hide loading indicator when video is ready
                             binding.loadingIndicator.visibility = View.GONE
                         } else binding.loadingIndicator.visibility = View.VISIBLE
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
                 ItemLoopVideoBinding.inflate(
                     LayoutInflater.from(parent.context), parent, false
                 )
             )
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

     }*/

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
        player = null
    }
}

