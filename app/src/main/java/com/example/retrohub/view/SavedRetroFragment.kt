package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.model_view.RetroConfirmViewModel
import kotlinx.android.synthetic.main.fragment_retro_saved.*
import org.koin.android.ext.android.inject


class SavedRetroFragment: MainActivity.RetroHubFragment(R.layout.fragment_retro_saved) {

    private val viewModel: RetroConfirmViewModel by inject()

    override fun getToolbarTitle() = "RetroHub"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading_view.isVisible = true
        viewModel.state.observe(viewLifecycleOwner) {
            if(it == RetroConfirmViewModel.State.OK) setOkResult()
            else setErrorResult()
            loading_view.isVisible = false
        }
        next_step.setOnClickListener {
            viewModel.resetInfo()
            findNavController().popBackStack()
            findNavController().navigate(R.id.mainFragment)
        }
        viewModel.saveRetro()
    }

    fun setOkResult() {
        icon_result.setImageDrawable(resources.getDrawable(R.drawable.ic_success, null))
        result_text.text = getString(R.string.retro_ok)
    }

    fun setErrorResult() {
        icon_result.setImageDrawable(resources.getDrawable(R.drawable.ic_error, null))
        result_text.text = getString(R.string.retro_error)
    }

}