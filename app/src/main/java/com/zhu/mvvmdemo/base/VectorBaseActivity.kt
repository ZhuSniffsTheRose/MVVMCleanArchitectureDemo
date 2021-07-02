package com.zhu.mvvmdemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class VectorBaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var views: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = getBinding()
        setContentView(views.root)
    }

    abstract fun getBinding(): VB

}
