package com.zhu.mvvmdemo.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.zhu.mvvmdemo.R
import com.zhu.mvvmdemo.databinding.ActivityMainBinding
import com.zhu.mvvmdemo.util.viewBindings


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val mViewBinding by viewBindings(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding.root.setBackgroundColor(Color.parseColor("#FF018786"))

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = PagingFragment()
        transaction.replace(R.id.sample_content_fragment, fragment)
        transaction.commit()

        listOf<String>().groupBy {

        }
    }
}