package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.showDialog
import kotlinx.android.synthetic.main.fragment_add_team.*
import kotlinx.android.synthetic.main.view_card_grid.view.*


class AddTeamFragment : MainActivity.RetroHubFragment(R.layout.fragment_add_team) {

    private var nameList = emptyList<String>()

    override fun getToolbarTitle() = getString(R.string.add_team_toolbar)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_user.setOnClickListener {
            loading_view.isVisible = true
            val name = name_input.text.text
            if(name.isNullOrBlank()) TODO("show error dialog")
            else TODO("call services")
        }

        next_step.setOnClickListener {
            loading_view.isVisible = true
            TODO("call services save team")
        }
    }

    fun onSearchError() {
        showDialog(R.string.user_not_found_title, R.string.user_not_found, R.string.accept_button) {}
    }

    fun updateMembersList() {
        var list = ""
        nameList.forEachIndexed { index, s ->
            list += when {
                index < nameList.size - 2 -> "$s, "
                index < nameList.size - 1 -> "$s y "
                else -> s
            }
        }
        list_usernames.text = list
    }

    fun closeProcess() {
        loading_view.isVisible = false
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainFragment)
    }
}