package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.model_view.PersistedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_empty_view.*
import org.koin.android.ext.android.inject


class MainFragment: MainActivity.RetroHubFragment(R.layout.fragment_empty_view) {

    private val persistedVM: PersistedViewModel by inject()

    override fun getToolbarTitle() = "RetroHub"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        persistedVM.getUserName().observe(viewLifecycleOwner) {
            welcome_text.text = getString(R.string.welcome_message) + " $it!"
        }
        floating_action_button.setOnClickListener {
            findNavController().navigate(R.id.selectionTypeFragment)
        }
    }


}