package com.example.retrohub.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.retrohub.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.setVisibilityViews(isVisible: Boolean , views: List<View>) = views.forEach { it.isVisible = isVisible }
fun Fragment.hideViews(views: List<View>) = setVisibilityViews(false, views)
fun Fragment.showViews(views: List<View>) = setVisibilityViews(true,views)

fun Fragment.getColor(@ColorRes color: Int) = resources.getColor(color)

fun Fragment.showDialog(@StringRes title: Int, @StringRes message: Int, accept:()->Unit) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(R.string.decline_button) { dialog, which ->
            dialog.dismiss()
        }
        .setPositiveButton(R.string.accept_button) { dialog, which ->
            dialog.dismiss()
            accept.invoke()
        }.show()
}

fun Fragment.showDialog(@StringRes title: Int, @StringRes message: Int, @StringRes neutral: Int, action:()->Unit) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setNeutralButton(neutral) { dialog, which ->
            action.invoke()
            dialog.dismiss()
        }.show()
}