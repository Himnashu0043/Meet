package org.meetcute.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import org.meetcute.R
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.User
import org.meetcute.databinding.CardBasicBinding
import org.meetcute.databinding.CardStoriesBinding
import org.meetcute.databinding.FragmentProfileBinding
import org.meetcute.databinding.ItemHashtagsBinding
import org.meetcute.appUtils.BasicInfo
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.EditType
import org.meetcute.appUtils.GridItemDecoration
import org.meetcute.appUtils.ScreenType
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.loadCircleCrop
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.model.StoryItem
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.view.activities.AddStoryActivity
import org.meetcute.view.activities.GoLiveActivity
import org.meetcute.view.activities.ViewStoryActivity
import org.meetcute.viewModel.ProfileViewModel

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    private val hashTagAdapter: HashTagAdapter by lazy {
        HashTagAdapter()
    }

    private val storiesAdapter: StoriesAdapter by lazy {
        StoriesAdapter()
    }

    private val basicInfoAdapter: BasicInfoAdapter by lazy {
        BasicInfoAdapter()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing_basic_info)
        binding.rvBasicInfo.layoutManager = layoutManager
        binding.rvBasicInfo.addItemDecoration(GridItemDecoration(spacingInPixels))
        binding.rvBasicInfo.adapter = basicInfoAdapter


        val layoutManagerHashTag = FlexboxLayoutManager(requireContext())
        layoutManagerHashTag.flexDirection = FlexDirection.ROW
        val spacingInPixelsHashTag = resources.getDimensionPixelSize(R.dimen.grid_spacing_hash_tags)
        binding.rvHashtags.layoutManager = layoutManagerHashTag
        binding.rvHashtags.addItemDecoration(
            GridItemDecoration(
                spacingInPixelsHashTag
            )
        )
        binding.rvHashtags.adapter = hashTagAdapter


        viewModel.profileResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            pref.user = it.value?.data
                            setDetails(it.value?.data)
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
            }
        }
        viewModel.allStoriesResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            storiesAdapter.clear()
                            storiesAdapter.add(null)
                            storiesAdapter.add(it.value?.data ?: emptyList())
                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        it.throwable?.message?.show(binding)
                    }

                    else -> {}
                }
            }
        }

        // details
        setDetails(pref.user)

        // stories
        binding.rvStories.adapter = storiesAdapter
        storiesAdapter.add(null)

        binding.viewPager.adapter = ViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(getDrawable(requireContext(), R.drawable.loopimg))
                    tab.text = "Loop Videos"
                }

                else -> {
                    tab.setIcon(getDrawable(requireContext(), R.drawable.image_icon))
                    tab.text = "Images"
                }
            }
        }.attach()

        binding.rlGoLive.setOnClickListener {
            startActivity(Intent(requireActivity(), GoLiveActivity::class.java))
        }

        binding.ivCrown.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.UPLOAD_PROFILE
                )
            )
        }

        binding.llVideoCallRate.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.VIDEO_CALL_RATE
                )
            )
        }

        binding.ivAdditionalInfo.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.ADDITIONAL
                )
            )
        }

        binding.ivEditLoopCall.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.UPLOAD_IMAGE_VIDEO
                )
            )
        }

        binding.ivEditBio.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.ADDITIONAL
                )
            )
        }

        binding.ivEditBasicInfo.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.BASIC
                )
            )
        }

        binding.ivEditHashtag.setOnClickListener {
            startActivity(
                BasicInfoActivity.editIntent(
                    requireContext(),
                    EditType.SINGLE,
                    ScreenType.HASHTAG
                )
            )
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfile()
        viewModel.getAllStories()
    }

    private fun setUpHashTags(user: User?) {
        hashTagAdapter.set(user?.hashTags ?: emptyList())
    }

    private val basicInfo = mutableListOf<BasicInfo>()
    private fun setUpBasicInfo(user: User?) {
        user?.let {
            basicInfo.clear()
            basicInfo.add(BasicInfo(R.drawable.language, it.language))
            basicInfo.add(BasicInfo(R.drawable.age, "${Utils.getAge(it.dob)} year"))
            basicInfo.add(BasicInfo(R.drawable.height, it.height))
            basicInfo.add(BasicInfo(R.drawable.weight, it.weight))
            basicInfo.add(BasicInfo(R.drawable.model, it.profession))
            basicInfoAdapter.set(basicInfo)
        }
    }

    private fun setDetails(user: User?) {
        user?.let {
            binding.user = it
            setUpHashTags(it)
            setUpBasicInfo(it)
        }
    }

    inner class StoriesAdapter : BaseAdapter<StoryItem>() {

        inner class InfoViewHolder(val binding: CardStoriesBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    if (absoluteAdapterPosition != 0) startActivity(
                        Intent(
                            requireActivity(),
                            ViewStoryActivity::class.java
                        )
                    )
                    else startActivity(
                        Intent(
                            requireContext(), AddStoryActivity::class.java
                        )
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return InfoViewHolder(
                CardStoriesBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as InfoViewHolder).apply {
                get(position).let {
                    if (position == 0) {
                        binding.ivStories.visibility = View.GONE
                        binding.ivAddStory.visibility = View.VISIBLE
                    } else {
                        binding.ivStories.visibility = View.VISIBLE
                        binding.ivAddStory.visibility = View.GONE
                        binding.ivStories.loadCircleCrop(it?.fileUrl)
                        binding.ivStories.isSelected = !(it?.seen?:false)
                    }

                }
            }
        }
    }

    inner class BasicInfoAdapter : BaseAdapter<BasicInfo>() {

        inner class InfoViewHolder(val binding: CardBasicBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {}
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return InfoViewHolder(
                CardBasicBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as InfoViewHolder).binding.apply {
                tvChat.text = get(position)?.text
                ivImage.setImageResource(get(position)?.id ?: 0)
            }
        }
    }

    inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return ImageVideoFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
        }

    }

    inner class HashTagAdapter : BaseAdapter<String>() {

        inner class HashTagViewHolder(val binding: ItemHashtagsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return HashTagViewHolder(
                ItemHashtagsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as HashTagViewHolder).apply {
                binding.tvHashtags.text = "#${get(position)}"
                binding.tvHashtags.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }
    }

}