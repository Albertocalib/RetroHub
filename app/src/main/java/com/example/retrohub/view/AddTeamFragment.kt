package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.getString
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.TeamViewModel
import kotlinx.android.synthetic.main.fragment_add_team.*
import kotlinx.android.synthetic.main.view_card_grid.view.*
import org.koin.android.ext.android.inject
import java.util.*


class AddTeamFragment : MainActivity.RetroHubFragment(R.layout.fragment_add_team) {

    private var nameList = emptyList<String>().toMutableList()

    private val teamViewModel: TeamViewModel by inject()

    override fun getToolbarTitle() = getString(R.string.add_team_toolbar)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamViewModel.saveState.observe(viewLifecycleOwner) {
            if (it) closeProcess()
            else onServiceError()
        }
        teamViewModel.validUser.observe(viewLifecycleOwner,::isValidUser)

        add_user.setOnClickListener {
            val name = user_name_input.getString()
            if(name.isBlank()) user_name_input.error = getString(R.string.empty_user_name)
            else {
                loading_view.isVisible = true
                teamViewModel.validateUser(name)
            }
        }

        next_step.setOnClickListener {
            val name = name_input.getString()
            if (name.isBlank()) name_input.error = getString(R.string.empty_user_name)
            else if (nameList.isEmpty()) Toast.makeText(requireContext(), "Su equipo está vacío.", Toast.LENGTH_LONG).show()
            else {
                loading_view.isVisible = true
                teamViewModel.createTeam(nameList, name)
            }
        }
    }

    private fun isValidUser(isValid: Boolean) {
        if(!isValid) onSearchError()
        else updateMembersList(user_name_input.getString())
        user_name_input.error = null
        user_name_input.editText?.setText("")
    }

    private fun onServiceError() {
        loading_view.isVisible = false
        showDialog(R.string.error_message_title,R.string.error_message_service,R.string.accept_button){ }
    }

    private fun onSearchError() {
        loading_view.isVisible = false
        showDialog(R.string.user_not_found_title, R.string.user_not_found, R.string.accept_button) { }
    }

    private fun updateMembersList(name: String) {
        loading_view.isVisible = false
        nameList.add(name.toLowerCase(Locale.ROOT))
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

    private fun closeProcess() {
        loading_view.isVisible = false
        findNavController().popBackStack()
        findNavController().navigate(R.id.mainFragment)
    }
}