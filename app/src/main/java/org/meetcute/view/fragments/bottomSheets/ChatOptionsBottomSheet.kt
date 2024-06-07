package org.meetcute.view.fragments.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import org.meetcute.R
import org.meetcute.appUtils.blur.BlurDialog
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.BottomSheetChatOptionsBinding
import org.meetcute.databinding.DialogDeleteaccountBinding
import org.meetcute.databinding.DialogsBlockBinding
import org.meetcute.databinding.ReportVenezuelaBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.SupportViewModel

class ChatOptionsBottomSheet(val blockId: String, val id: String) : BaseBottomSheet() {
    private val viewModel: SupportViewModel by activityViewModels()
    private var getText: String? = ""
    private val binding: BottomSheetChatOptionsBinding by lazy {
        BottomSheetChatOptionsBinding.inflate(layoutInflater)
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
        println("================oprin$blockId")
        binding.ivBack.setOnClickListener { dismiss() }
        binding.ivDelete.setOnClickListener {
            deleteChat()
        }
        binding.ivBlock.setOnClickListener {
            block()

        }
        binding.ivReport.setOnClickListener {
            report()
        }
    }


    fun report() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            ReportVenezuelaBinding.inflate(layoutInflater).let { nameBinding ->
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(nameBinding.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                nameBinding.tvFake.setOnClickListener {
                    it.isSelected = true
                    getText = "FakeProfile"
                    println("============$getText")
                    nameBinding.tvNeg.isSelected = false
                    nameBinding.tvInapp.isSelected = false
                    nameBinding.tvRude.isSelected = false
                    nameBinding.tvLorem.isSelected = false
                }
                nameBinding.tvNeg.setOnClickListener {
                    it.isSelected = true
                    getText = "Negative Attitude"
                    nameBinding.tvFake.isSelected = false
                    nameBinding.tvInapp.isSelected = false
                    nameBinding.tvRude.isSelected = false
                    nameBinding.tvLorem.isSelected = false
                }
                nameBinding.tvInapp.setOnClickListener {
                    it.isSelected = true
                    getText = "Inappropriate Content"
                    nameBinding.tvFake.isSelected = false
                    nameBinding.tvNeg.isSelected = false
                    nameBinding.tvRude.isSelected = false
                    nameBinding.tvLorem.isSelected = false
                }
                nameBinding.tvRude.setOnClickListener {
                    it.isSelected = true
                    getText = "Rude Behavior"
                    nameBinding.tvFake.isSelected = false
                    nameBinding.tvInapp.isSelected = false
                    nameBinding.tvNeg.isSelected = false
                    nameBinding.tvLorem.isSelected = false
                }
                nameBinding.tvLorem.setOnClickListener {
                    it.isSelected = true
                    getText = "Lorem Ipsum"
                    nameBinding.tvFake.isSelected = false
                    nameBinding.tvInapp.isSelected = false
                    nameBinding.tvNeg.isSelected = false
                    nameBinding.tvRude.isSelected = false
                }
                nameBinding.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
                nameBinding.flDontAllow.setOnClickListener {

                    if (!getText.isNullOrEmpty()) {
                        mDialog.dismiss()
                        dismiss()
                        viewModel.reportViewer(blockId, getText ?: "")
                    } else {
                        Toast.makeText(
                            nameBinding.root.context,
                            "Please Select Reason",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun block() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogsBlockBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btbNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                    dismiss()
                    viewModel.blockedUsers(blockId)

                }
            }
        }
    }

    private fun deleteChat() {
        BlurDialog(requireActivity(), R.style.DialogStyle).let { mDialog ->
            DialogDeleteaccountBinding.inflate(layoutInflater).let {
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog.setCancelable(false)
                mDialog.setCanceledOnTouchOutside(false)
                mDialog.setContentView(it.root)
                mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                mDialog.show()
                it.btnNo.setOnClickListener {
                    mDialog.dismiss()
                }
                it.flDontAllow.setOnClickListener {
                    mDialog.dismiss()
                    dismiss()
                    viewModel.deleteChatHistory(id)
                }
            }
        }
    }


}