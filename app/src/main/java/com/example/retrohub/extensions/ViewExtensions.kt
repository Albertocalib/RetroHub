package com.example.retrohub.extensions

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getString(): String {
    val s = this.editText?.text?.toString()?:""
    return if(s == "null") "" else s
}