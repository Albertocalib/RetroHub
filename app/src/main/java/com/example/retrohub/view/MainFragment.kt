package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_empty_view.*


class MainFragment: MainActivity.RetroHubFragment(R.layout.fragment_empty_view) {

    override fun getToolbarTitle() = "RetroHub"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        icon_empty_box.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        floating_action_button.setOnClickListener {
            findNavController().navigate(R.id.selectionTypeFragment)
        }
    }


}