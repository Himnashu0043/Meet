package org.meetcute.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.meetcute.R
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.common.Utils.dp
import org.meetcute.network.data.model.Post
import org.meetcute.databinding.ItemUploadImageBinding
import org.meetcute.databinding.MutipleUploadLayoutBinding


class UploadLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: MutipleUploadLayoutBinding
    private var imageList = arrayListOf<Post?>().apply {
        add(null)
        add(null)
        add(null)
        add(null)
        add(null)
    }
    private val myAdapter: MyAdapter by lazy {
        MyAdapter { id: String, position: Int ->
            onImageDeleted(id, position)
        }
    }

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.mutiple_upload_layout, this, true
        )
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditTextAttributes)
        val title = a.getString(R.styleable.AppEditTextAttributes_title)
        val buttonText = a.getString(R.styleable.AppEditTextAttributes_buttonText)
        val uploadNote = a.getString(R.styleable.AppEditTextAttributes_uploadNote)
        a.recycle()
        binding.root.updatePadding(0, 24.dp(context), 0, 24.dp(context))
        binding.tvTitle.text = title
        binding.tvNote.visibility = if (uploadNote.isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.tvNote.text = uploadNote
        binding.btnUpload.text = buttonText
        binding.rvUploaded.adapter = myAdapter
        myAdapter.set(imageList)
        clipChildren = true
        clipToPadding = true
    }


    var onImageDeleted = { id: String, position: Int -> }

    private fun findLastAddedPhotoPosition(): Int {
        var position = -1
        for (i in 0 until myAdapter.itemCount) {
            if (myAdapter.get(i) == null) {
                position = i
                break
            }
        }
        return position
    }


    fun getAllImages(): MutableList<Post?> {
        return myAdapter.getAll()
    }

    fun imagesCompleted(): Boolean {
        return findLastAddedPhotoPosition() == -1
    }


    fun clear(){
        myAdapter.clear()
    }
    fun addPhoto(data: Post?) {
        myAdapter.getAll()
        val position = findLastAddedPhotoPosition()
        if (position != -1) {
            myAdapter.changeItemAt(data, position)
        }
    }

    fun addLocalPhoto(data: String?) {
        val post = Post(0, "", false, "", "", false, "", data ?: "", "", "")
        myAdapter.getAll()
        val position = findLastAddedPhotoPosition()
        if (position != -1) {
            myAdapter.changeItemAt(post, position)
        }
    }


    fun updatePhoto(data: Post?, position: Int) {
        myAdapter.changeItemAt(data, position)
    }

    fun visibleInfo() {
        findViewById<ImageView>(R.id.ivInfo).visibility = View.VISIBLE
    }

    fun goneInfo() {
        findViewById<ImageView>(R.id.ivInfo).visibility = View.GONE
    }

    fun setList(list: List<Post?>) {
        myAdapter.set(list)
    }

    fun setOnButtonClickListener(onClick: () -> Unit) {
        binding.btnUpload.setOnClickListener { onClick.invoke() }
    }

    inner class MyAdapter(val handle: (id: String, position: Int) -> Unit) : BaseAdapter<Post?>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                ItemUploadImageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ViewHolder).apply {
                if (get(position) == null) {
                    binding.ivDelete.visibility = View.GONE
                    binding.ivImage.visibility = View.GONE
                } else {
                    Glide.with(binding.ivImage).load(get(position)?.fileUrl).into(binding.ivImage)
                    binding.ivDelete.visibility = View.VISIBLE
                    binding.ivImage.visibility = View.VISIBLE
                }
            }
        }

        inner class ViewHolder(val binding: ItemUploadImageBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.ivDelete.setOnClickListener {
                    if (!get(absoluteAdapterPosition)?._id.isNullOrEmpty())
                        handle.invoke(
                            get(absoluteAdapterPosition)?._id ?: "",
                            absoluteAdapterPosition
                        )
                    changeItemAt(null, absoluteAdapterPosition)
                }
            }
        }

    }
}