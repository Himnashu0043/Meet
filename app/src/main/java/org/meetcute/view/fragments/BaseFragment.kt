package org.meetcute.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.meetcute.appUtils.MeetCute.MeetCute
import org.meetcute.appUtils.preferance.PreferenceHelper
import javax.inject.Inject

abstract class BaseFragment<viewBinding : ViewBinding> : Fragment() {


    lateinit var pref: PreferenceHelper

    private var _binding: viewBinding? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = MeetCute.app.pref
    }

    abstract fun getLayoutId():Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }
}