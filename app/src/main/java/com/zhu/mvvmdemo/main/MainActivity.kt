package com.zhu.mvvmdemo.main

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhu.mvvmdemo.R
import com.zhu.mvvmdemo.util.viewBindings
import com.zhu.mvvmdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val mViewBinding by viewBindings(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding.root.setBackgroundColor(Color.parseColor("#FF018786"))
    }
}