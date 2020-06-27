package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.max
import com.example.retrohub.model_view.PersonalAreaViewModel
import kotlinx.android.synthetic.main.fragment_personal_area_view.*
import org.koin.android.ext.android.inject


class PersonalAreaFragment : MainActivity.RetroHubFragment(R.layout.fragment_personal_area_view) {

    val viewModel: PersonalAreaViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.numberOfRetros.observe(viewLifecycleOwner, ::setNumberOfRetros)
        viewModel.userName.observe(viewLifecycleOwner){ s -> info_user_name.text = s}
        viewModel.name.observe(viewLifecycleOwner){ s -> user_name.text = s}
        viewModel.getNumberOfRetrospectives()
    }

    private fun setNumberOfRetros(data: Pair<Int, Int>) {
        emtpy_group.isVisible = data.first == 0 && data.second == 0
        group_graph.isVisible = !emtpy_group.isVisible
        val div = maxOf(data.max(),1)
        progress_bar_current.progress = ((data.first.toDouble()/div)*100).toInt()
        progress_bar_past.progress = ((data.second.toDouble()/div)*100).toInt()
    }
}