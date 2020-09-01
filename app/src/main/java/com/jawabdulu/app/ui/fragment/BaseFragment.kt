package com.jawabdulu.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.jawabdulu.app.viewModel.QuizViewModel
import com.jawabdulu.app.ui.activity.MainActivity

abstract class BaseFragment(@LayoutRes contentLayoutId : Int) : Fragment(contentLayoutId) {

    lateinit var viewModel: QuizViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }
}