package com.zhu.mvvmdemo.base

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding

/**
 * @author Zhu
 * @date 2021/5/6 10:41 AM
 * @desc
 */

inline fun <reified BindingT : ViewBinding> AppCompatActivity.viewBindings(
    crossinline bind: (View) -> BindingT
) = object : Lazy<BindingT> {

    private var initialized: BindingT? = null

    override val value: BindingT
        get() = initialized ?: bind(
            findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        ).also {
            initialized = it
        }

    override fun isInitialized() = initialized != null
}

inline fun <reified BindingT : ViewBinding> Fragment.viewBindings(
    crossinline bind: (View) -> BindingT
) = object : Lazy<BindingT> {

    private var initialized: BindingT? = null

    private val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            initialized = null
        }
    }

    override val value: BindingT
        get() = initialized ?: bind(requireView()).also {
            viewLifecycleOwner.lifecycle.addObserver(observer)
            initialized = it
        }

    override fun isInitialized() = initialized != null
}
