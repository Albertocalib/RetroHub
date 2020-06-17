package com.example.retrohub.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

fun Fragment.setVisibilityViews(isVisible: Boolean , views: List<View>) = views.forEach { it.isVisible = isVisible }
fun Fragment.hideViews(views: List<View>) = setVisibilityViews(false, views)
fun Fragment.showViews(views: List<View>) = setVisibilityViews(true,views)

fun Fragment.getColor(@ColorRes color: Int) = resources.getColor(color)