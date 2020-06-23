package com.example.retrohub.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_register_view.*

class RegisterFragment : MainActivity.RetroHubFragment(R.layout.fragment_register_view) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar.menu.clear()
        requireActivity().toolbar.title = getString(R.string.register_toolbar)
        save_button.setOnClickListener {
            Toast.makeText(requireContext(),R.string.work_in_progress,Toast.LENGTH_SHORT).show()
        }
    }
}