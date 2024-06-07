package org.meetcute.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentSetHashTagBinding
import org.meetcute.databinding.ItemHashtagsBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.GridItemDecoration
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.viewModel.BasicInfoViewModel


@AndroidEntryPoint
class SetHashTagFragment : BaseFragment<FragmentSetHashTagBinding>() {

    private val viewModel: BasicInfoViewModel by activityViewModels()
    private val hashTagAdapter: HashTagAdapter by lazy {
        HashTagAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_set_hash_tag
    }

    private var editType: String = ""
    private var screenType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editType = it.getString("EditType", "")
            screenType = it.getString("ScreenType", "")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.isEditMode = editType.isNotEmpty() || screenType.isNotEmpty()
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing_hash_tags)
        binding.rvHashTags.layoutManager = layoutManager
        binding.rvHashTags.addItemDecoration(GridItemDecoration(spacingInPixels))
        binding.rvHashTags.adapter = hashTagAdapter

        if (editType.isNotEmpty() && screenType.isNotEmpty()) {
            binding.flNext.showIcon(false)
            binding.flNext.setButtonText("Update")
            (requireActivity() as BasicInfoActivity).setTitleAndPage("Edit Hashtags", -1)
        } else
            (requireActivity() as BasicInfoActivity).setTitleAndPage("Setup Hashtags", 5)


        viewModel.hashTagResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {
                    is NetworkResponse.Loading -> Loaders.showApiLoader(requireContext())
                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                            pref.user = it.value?.data
                            if (editType.isNotEmpty() && screenType.isNotEmpty())
                                requireActivity().finish()
                            else
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.flBasicInfo, SetUpVideoCallRateFragment())
                                    .addToBackStack("")
                                    .commit()
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hideApiLoader()
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
            }
        }

        binding.flNext.setOnClickListener {
            val hashTags = hashTagAdapter.getAll()
            if (hashTags.isEmpty())
                viewModel.hashTag.error.set("Please add hash tags")
            else viewModel.updateHashTag(hashTags)
        }

        binding.etHashtag.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val hashTag = viewModel.hashTag.text.get() ?: ""
                if (hashTag.isNotEmpty()) {
                    if (hashTagAdapter.itemCount < 15) {
                        if (!hashTagAdapter.isDuplicate(hashTag)) {
                            hashTagAdapter.add(hashTag)
                            viewModel.hashTag.text.set("")
                        } else {
                            viewModel.hashTag.text.set("")
                            "HashTag already added".show(binding)
                        }
                    } else {
                        viewModel.hashTag.text.set("")
                        "Only 15 HashTags can be added".show(binding)
                    }
                }
                return@setOnEditorActionListener true

            }
            false
        }

        hashTagAdapter.set(pref.user?.hashTags ?: emptyList())
    }

    inner class HashTagAdapter : BaseAdapter<String>() {

        fun isDuplicate(hashTag: String): Boolean {
            return itemList.find { it.equals(hashTag) } != null
        }

        inner class HashTagViewHolder(val binding: ItemHashtagsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.tvHashtags.setOnClickListener {
                    removeAt(absoluteAdapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return HashTagViewHolder(
                ItemHashtagsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as HashTagViewHolder).binding.tvHashtags.text = "#${get(position)}"
        }
    }

}