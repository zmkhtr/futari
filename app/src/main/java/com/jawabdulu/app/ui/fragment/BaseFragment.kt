package com.jawabdulu.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.jawabdulu.app.viewModel.QuizViewModel
import com.jawabdulu.app.ui.activity.MainActivity
import com.jawabdulu.app.viewModel.AppViewModel
import com.jawabdulu.app.viewModel.BigBoxApiViewModel

abstract class BaseFragment(@LayoutRes contentLayoutId : Int) : Fragment(contentLayoutId) {

    lateinit var viewModel: QuizViewModel
    lateinit var viewModelApp: AppViewModel
    lateinit var viewModelBigBox: BigBoxApiViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = (activity as MainActivity).viewModel
//        viewModelApp = (activity as MainActivity).viewModelApp
//        viewModelBigBox = (activity as MainActivity).viewModelBigBox
    }

    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getPersistentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, layout: Int): View? {
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater?.inflate(layout,container,false)
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.getParent() as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }
}