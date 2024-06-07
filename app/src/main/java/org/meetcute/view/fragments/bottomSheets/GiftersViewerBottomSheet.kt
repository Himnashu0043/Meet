package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.DialogGiftersViewersBinding
import org.meetcute.databinding.ItemGiftersViewersBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.GiftByBroadcastListRes
import org.meetcute.viewModel.BroadcastViewModel
import org.meetcute.viewModel.SupportViewModel
import java.util.Locale

class GiftersViewerBottomSheet : BaseBottomSheet() {
    private var list = ArrayList<GiftByBroadcastListRes.Data>()
    private val viewModel: SupportViewModel by activityViewModels()
    private val viewModel1: BroadcastViewModel by activityViewModels()
    private val adapter: GiftRequestsAdapter by lazy {
        GiftRequestsAdapter()
    }


    private val binding: DialogGiftersViewersBinding by lazy {
        DialogGiftersViewersBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCross.setOnClickListener {
            dismiss()
        }
        viewModel.giftByBroadCastResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {
                        Loaders.show(requireActivity())
                    }
                    is NetworkResponse.Success -> {
                        Loaders.hide()
                        if (it.value?.success == true) {
                            list.clear()
                            list.addAll(it.value?.data!!)
                            binding.rvGifters.adapter = adapter
                            adapter.set(it.value?.data ?: emptyList())
                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hide()
                        it.throwable?.message?.show(binding)
                    }
                    else -> {}
                }
        }
        viewModel.giftByBroadCast(viewModel1.currentBrId)

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var text = p0.toString().trim()
                if (text.isNotEmpty()) {
                    if (text.length > 0) {
                        var tempFilterList = list.filter {
                            it.viewer?.get(0)?.name!!.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT))
                        }
                        binding.rvGifters.adapter = adapter
                        adapter.set(tempFilterList)
                        adapter!!.notifyDataSetChanged()
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Data not Found!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    binding.rvGifters.adapter = adapter
                    adapter.set(list)
                    adapter!!.notifyDataSetChanged()

                }
            }
        })


    }


    inner class GiftRequestsAdapter : BaseAdapter<GiftByBroadcastListRes.Data>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return TagGiftViewHolder(
                ItemGiftersViewersBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as TagGiftViewHolder).apply {
                binding.tvVideoName.text =
                    get(absoluteAdapterPosition)?.viewer?.get(0)?.name ?: ""
                binding.tvMin.text = "${get(absoluteAdapterPosition)?.coin ?: 0}"
                binding.ivVideoCall.load(
                    get(absoluteAdapterPosition)?.viewer?.get(0
                    )?.profileImage ?: ""
                )
                if (get(absoluteAdapterPosition)?.viewer?.get(0)?.online != true) {
                    binding.imageView4.visibility = View.INVISIBLE
                } else {
                    binding.imageView4.visibility = View.VISIBLE
                }
            }
        }

        inner class TagGiftViewHolder(val binding: ItemGiftersViewersBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.ivSmallCoin.setOnClickListener {
//                    dismiss()
//                    EndBroadcastBottomSheet().show(requireActivity().supportFragmentManager, "")
                }
                binding.tvSendGreetings.setOnClickListener {
                    dismiss()
                    /* CommentsBottomSheet().show(requireActivity().supportFragmentManager, "")*/
                }

            }
        }

    }

}