package com.zhu.mvvmdemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhu.mvvmdemo.R

/**
 * @author Zhu
 * @date 2021/7/2 15:34
 * @desc
 */
abstract class BaseViewBidingFragment<VB : ViewBinding> : Fragment() {

    var _binding: VB? = null

    // This property is only valid between onCreateView and onDestroyView.
    protected val views: VB
        get() = _binding!!


    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getBinding(inflater, container)
        return views.root
    }


    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /* ==========================================================================================
    * Common Dialogs
    * ========================================================================================== */
    protected fun displayErrorDialog(msg: String) {
        MaterialAlertDialogBuilder(requireActivity())
                .setTitle("ERROR")
                .setMessage(msg)
                .setPositiveButton("ok", null)
                .show()
    }
}