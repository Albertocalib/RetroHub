package com.example.retrohub.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import kotlinx.android.synthetic.main.fragment_empty_view.*

class MainFragment: MainActivity.RetroHubFragment(R.layout.fragment_empty_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        icon_empty_box.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}