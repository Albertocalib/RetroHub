package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.getString
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.PersistedRetroViewModel
import com.example.retrohub.model_view.State
import kotlinx.android.synthetic.main.fragment_selection_type.next_step
import kotlinx.android.synthetic.main.fragment_title.*
import org.koin.android.ext.android.inject


class TitleRetroFragment () : MainActivity.RetroHubFragment(R.layout.fragment_title) {

    private val persistedRetroViewModel: PersistedRetroViewModel by inject()

    override fun getToolbarTitle() = getString(R.string.title_view_toolbar)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        persistedRetroViewModel.state.observe(viewLifecycleOwner) {
            when(it){
                State.SAVING -> setLoadingVisibility(true)
                State.ERROR -> showDialog(R.string.error_message_title,R.string.empty_error_message) {
                    title_input.error = getString(R.string.empty_error_message)
                }
                State.SAVED -> {
                    setLoadingVisibility(false)
                    findNavController().navigate(R.id.fillRetroFragment)
                }
            }
        }
        next_step.setOnClickListener {
            persistedRetroViewModel.updateRetro(title_input.getString())
        }

    }



    private fun setLoadingVisibility(loading: Boolean) {
        loading_title_view.isVisible = loading
        input_view.isVisible = !loading
    }
}