package org.meetcute.view.activities

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.meetcute.R
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.databinding.ActivityPrivacyAndTermBinding
import org.meetcute.network.data.NetworkResponse
import org.meetcute.viewModel.SupportViewModel
@AndroidEntryPoint
class PrivacyAndTermActivity : BaseActivity<ActivityPrivacyAndTermBinding>() {
    private val viewModel: SupportViewModel by viewModels()
    private var from: String = ""
    override fun getLayout(): Int {
        return R.layout.activity_privacy_and_term
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        binding.ivBack.setOnClickListener {
            finish()
        }
        viewModel.getPrivacyAndPolicyResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(this)
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        binding.tvTitle.text = it.value?.data?.get(0)?.title ?: ""
                        val htmlAsString = it.value?.data?.get(0)?.description
                        val htmlAsSpanned = Html.fromHtml(htmlAsString)
                        binding.tvDes.text = htmlAsSpanned
                    } else it.value?.message?.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }
        viewModel.getTermAndConditionResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(this)
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        binding.tvTitle.text = it.value?.data?.get(0)?.title ?: ""
                        val htmlAsString = it.value?.data?.get(0)?.description
                        val htmlAsSpanned = Html.fromHtml(htmlAsString)
                        binding.tvDes.text = htmlAsSpanned
                    } else it.value?.message?.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }
        viewModel.getAboutUsResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(this)
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        binding.tvTitle.text = "Contact Us"
                        val htmlAsString = it.value?.data?.get(0)?.description
                        val htmlAsSpanned = Html.fromHtml(htmlAsString)
                        binding.tvDes.text = htmlAsSpanned
                    } else it.value?.message?.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }


    }

    override fun onResume() {
        super.onResume()
        when (from){
            "term" ->{
                viewModel.getTermAndCondition()
            }
            "about" ->{
                viewModel.getAboutUs()
            }
            else ->{
                viewModel.getPrivacyAndPolicy()
            }
        }
        /*  if (from == "term") {
              viewModel.getTermAndCondition()
          } else if (from == "about") {
              viewModel.getAboutUs()
          } else {
              viewModel.getPrivacyAndPolicy()
          }*/
    }
}