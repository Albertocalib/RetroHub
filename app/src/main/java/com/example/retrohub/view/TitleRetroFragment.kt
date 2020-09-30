package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.getString
import com.example.retrohub.extensions.inflate
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.PersistedRetroViewModel
import com.example.retrohub.model_view.PersistedViewModel
import com.example.retrohub.model_view.State
import com.example.retrohub.model_view.TeamViewModel
import com.example.retrohub.view.mobile.Team
import kotlinx.android.synthetic.main.fragment_selection_type.next_step
import kotlinx.android.synthetic.main.fragment_title.*
import kotlinx.android.synthetic.main.view_card_type.view.*
import org.koin.android.ext.android.inject


class TitleRetroFragment : MainActivity.RetroHubFragment(R.layout.fragment_title),
    AdapterTeam.ViewHolder.Action {

    private val persistedRetroViewModel: PersistedRetroViewModel by inject()
    private val persistedUserViewModel: PersistedViewModel by inject()
    private val teamViewModel: TeamViewModel by inject()
    private var selectedIndex = -1
    private lateinit var adapter: AdapterTeam
    private var teams: List<Team> = emptyList()

    override fun getToolbarTitle() = getString(R.string.title_view_toolbar)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        persistedUserViewModel.isScrumMaster().observe(viewLifecycleOwner) {
            teams_view.isVisible = false
            if(it.second) {
                val name = it.first
                loading_small_anim.isVisible = true
                teamViewModel.getTeams(name)
            }
        }

        teamViewModel.teams.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) {
                teams_view.isVisible = false
                return@observe
            }
            with(team_list) {
                this@TitleRetroFragment.teams = it
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = AdapterTeam(it.map { it.name }.toTypedArray(),
                    this@TitleRetroFragment
                ).also { this@TitleRetroFragment.adapter = it }
                teams_view.isVisible = true
                loading_small_anim.isVisible = false
            }
        }
        persistedRetroViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                State.SAVING -> setLoadingVisibility(true)
                State.ERROR -> showDialog(
                    R.string.error_message_title,
                    R.string.empty_error_message
                ) {
                    title_input.error = getString(R.string.empty_error_message)
                }
                State.SAVED -> {
                    setLoadingVisibility(false)
                    findNavController().navigate(R.id.fillRetroFragment)
                }
            }
        }
        next_step.setOnClickListener {
            val list = if(selectedIndex == -1) emptyList() else teams[selectedIndex].team
            persistedRetroViewModel.updateRetro(title_input.getString(), list)
        }

    }

    override fun onItemSelected(item: Int) {
        if (selectedIndex != -1) adapter.notifyItemChanged( selectedIndex )
        selectedIndex = item
    }

    private fun setLoadingVisibility(loading: Boolean) {
        loading_title_view.isVisible = loading
        input_view.isVisible = !loading
    }
}

class AdapterTeam( val items: Array<String>, val listener: ViewHolder.Action ) :
    RecyclerView.Adapter<AdapterTeam.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.view_card_type))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], listener)

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        interface Action {
            fun onItemSelected(item: Int)
        }

        fun bind(item: String, listener: Action) = with(itemView) {
            name.text = item
            fragment_container_selection_type.isChecked = isSelected
            fragment_container_selection_type.setOnClickListener {
                if (!fragment_container_selection_type.isChecked) {
                    fragment_container_selection_type.isChecked = true
                    listener.onItemSelected(adapterPosition)
                }
            }
        }
    }

}
