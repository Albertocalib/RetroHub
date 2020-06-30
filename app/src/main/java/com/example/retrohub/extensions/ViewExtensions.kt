package com.example.retrohub.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getString(): String {
    val s = this.editText?.text?.toString()?:""
    return if(s == "null") "" else s
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.getString(@StringRes res: Int) = context.getString(res)