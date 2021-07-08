package com.zhu.mvvmdemo.extensions

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText

/**
 * @author Zhu
 * @date 2021/7/5 10:11
 * @desc
 */
// when the user taps the "Done" button in the on screen keyboard, save the item.
fun EditText.doneOnKeyboard(doThings: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {  //可以在edittext配置，  如果是 search 就是 IME_ACTION_GO
            doThings()
            return@setOnEditorActionListener true
        }
        false // action that isn't DONE occurred - ignore
    }
}

// When the user clicks on the button, or presses enter, save the item.
fun EditText.downOrEnterOnKeyboard(doThings: () -> Unit) {
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            doThings()
            return@setOnKeyListener true
        }
        false // event that isn't DOWN or ENTER occurred - ignore
    }
}

