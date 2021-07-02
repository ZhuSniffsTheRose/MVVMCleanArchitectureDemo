package com.zhu.mvvmdemo.utils

import android.content.Context
import android.widget.Toast

/**
 * @author Zhu
 * @date 2021/7/1 16:24
 * @desc
 */

fun Context.toast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}