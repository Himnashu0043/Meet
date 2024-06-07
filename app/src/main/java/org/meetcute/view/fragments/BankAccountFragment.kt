package org.meetcute.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.network.data.NetworkResponse
import org.meetcute.databinding.FragmentBankAccountsBinding
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.view.activities.BasicInfoActivity
import org.meetcute.view.fragments.dialogFragments.VerificationDialogFragment
import org.meetcute.viewModel.BasicInfoViewModel

@AndroidEntryPoint
class BankAccountFragment : BaseFragment<FragmentBankAccountsBinding>() {

    private val viewModel: BasicInfoViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_bank_accounts
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
        viewModel.isEditMode = editType.isNotEmpty() && screenType.isNotEmpty()
        if (editType.isNotEmpty() && screenType.isNotEmpty()) {
            binding.flNext.showIcon(false)
            binding.flNext.setButtonText("Update")
            (requireActivity() as BasicInfoActivity).setTitleAndPage("Bank Account", -1)
        } else
            (requireActivity() as BasicInfoActivity).setTitleAndPage("Bank Account", 7)

        viewModel.bankAccountResponse.observe(viewLifecycleOwner) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {

                    is NetworkResponse.Loading -> Loaders.showApiLoader(requireContext())

                    is NetworkResponse.Success -> {
                        Loaders.hideApiLoader()
                        if (it.value?.success == true) {
                           pref.user = it.value?.data
                            if (editType.isNotEmpty() && screenType.isNotEmpty())
                                requireActivity().finish()
                            else VerificationDialogFragment().show(
                                requireActivity().supportFragmentManager,
                                ""
                            )
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
            viewModel.bankAccount()
        }


    }

}