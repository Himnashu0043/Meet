package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import org.meetcute.R
import org.meetcute.databinding.BottomSheetCommentBinding
import org.meetcute.databinding.ChatSceenItemBinding
import org.meetcute.databinding.ItemCommentBottomBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.GridItemDecoration

class CommentsBottomSheet : BaseBottomSheet() {

    private val suggestionAdapter: SuggestionCommentAdapter by lazy {
        SuggestionCommentAdapter()
    }
    private val commentsAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    private val binding: BottomSheetCommentBinding by lazy {
        BottomSheetCommentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun setCommentsAdapter() {
        binding.rvGreetings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGreetings.adapter = commentsAdapter
        commentsAdapter.set((0..1).toList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        binding.rvGreetings.layoutManager = layoutManager
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.rvGreetings.addItemDecoration(GridItemDecoration(spacingInPixels))
        binding.rvGreetings.adapter = suggestionAdapter
        suggestionAdapter.set(message)
        binding.rvGreetings
    }

    private val message = mutableListOf<String>().apply {
        add("Hi, You are beautifull\uD83E\uDD79")
        add("I love your pic❤")
        add("❤❤❤❤")
        add("Hi")
        add("❤❤❤❤")
        add("Hi, You are beautifull\uD83E\uDD79")
        add("Hi")
    }


    inner class SuggestionCommentAdapter : BaseAdapter<String>() {

        inner class CommentViewHolder(val binding: ItemCommentBottomBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    setCommentsAdapter()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CommentViewHolder(
                ItemCommentBottomBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as CommentViewHolder).binding.tvSuggestion.text = get(position)
        }
    }

    inner class CommentAdapter : BaseAdapter<Int>() {

        inner class CommentViewHolder(val binding: ChatSceenItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CommentViewHolder(
                ChatSceenItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        }
    }

}