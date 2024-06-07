package org.meetcute.view.fragments.bottomSheets

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jp.wasabeef.blurry.Blurry
import org.meetcute.databinding.BottomSheetSettingsBinding

class LiveVideoSettingBottomSheet(val handle: (canJoinAnyFollow: String, canChatAnyFollow: String, mic: Boolean, sound: Boolean) -> Unit) :
    BaseBottomSheet() {
    private var canJoinAnyFollow: String? = "Anyone"
    private var canChatAnyfollow: String? = "Anyone"
    private var mic: Boolean = true
    private var sound: Boolean = true
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }


    private val binding: BottomSheetSettingsBinding by lazy {
        BottomSheetSettingsBinding.inflate(layoutInflater)
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

        binding.ivBack.setOnClickListener {
            dismiss()
            handle(canJoinAnyFollow ?: "", canChatAnyfollow ?: "", mic, sound)
        }
        binding.tvAnyoneChat.isSelected = true
        binding.tvAnyone.isSelected = true
        binding.tvAnyone.setOnClickListener {
            binding.tvAnyone.isSelected = true
            binding.tvFollower.isSelected = false
            canJoinAnyFollow = "Anyone"

        }
        binding.tvFollower.setOnClickListener {
            binding.tvAnyone.isSelected = false
            binding.tvFollower.isSelected = true
            canJoinAnyFollow = "Followers"
        }
        binding.tvAnyoneChat.setOnClickListener {
            binding.tvFollowerChat.isSelected = false
            binding.tvAnyoneChat.isSelected = true
            canChatAnyfollow = "Anyone"
        }
        binding.tvFollowerChat.setOnClickListener {
            binding.tvFollowerChat.isSelected = true
            binding.tvAnyoneChat.isSelected = false
            canChatAnyfollow = "Followers"
        }
        binding.ivMic.setOnClickListener {
            binding.ivMic.visibility = View.INVISIBLE
            binding.ivMicDis.visibility = View.VISIBLE
            mic = false
        }
        binding.ivMicDis.setOnClickListener {
            binding.ivMicDis.visibility = View.INVISIBLE
            binding.ivMic.visibility = View.VISIBLE
            mic = true
        }

        binding.ivSpeaker.setOnClickListener {
            binding.ivSpeaker.visibility = View.INVISIBLE
            binding.ivSpeakerDis.visibility = View.VISIBLE
            sound = false
        }
        binding.ivSpeakerDis.setOnClickListener {
            binding.ivSpeakerDis.visibility = View.INVISIBLE
            binding.ivSpeaker.visibility = View.VISIBLE
            sound = true
        }
    }

}