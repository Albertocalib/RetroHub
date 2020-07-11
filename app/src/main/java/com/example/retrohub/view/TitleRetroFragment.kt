package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.getString
import com.example.retrohub.extensions.inflate
import com.example.retrohub.extensions.setVisibilityViews
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.PersistedRetroViewModel
import com.example.retrohub.model_view.PersistedViewModel
import com.example.retrohub.model_view.State
import com.example.retrohub.view.mobile.Retro
import com.example.retrohub.view.mobile.RetroSubTypes
import com.example.retrohub.view.mobile.RetroType
import kotlinx.android.synthetic.main.fragment_selection_type.*
import kotlinx.android.synthetic.main.fragment_selection_type.next_step
import kotlinx.android.synthetic.main.fragment_title.*
import kotlinx.android.synthetic.main.view_card_type.view.*
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
                    Toast.makeText(requireContext(),"OK",Toast.LENGTH_SHORT).show()
                    //TODO: nav to next step
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