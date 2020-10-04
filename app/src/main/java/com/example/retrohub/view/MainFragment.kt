package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.getString
import com.example.retrohub.extensions.inflate
import com.example.retrohub.extensions.setVisibilityViews
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.PersistedViewModel
import com.example.retrohub.model_view.RetroViewModel
import com.example.retrohub.view.mobile.RetroSubTypes
import kotlinx.android.synthetic.main.fragment_empty_view.*
import kotlinx.android.synthetic.main.view_retro_preview_card.view.*
import org.koin.android.ext.android.inject


class MainFragment: MainActivity.RetroHubFragment(R.layout.fragment_empty_view) {

    private val persistedVM: PersistedViewModel by inject()
    private lateinit var adapter: PreviewRetroAdapter
    private val retroVM: RetroViewModel by inject()

    override fun getToolbarTitle() = "RetroHub"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading_view.isVisible = true
        persistedVM.getFirstName().observe(viewLifecycleOwner) {
            welcome_text.text = getString(R.string.welcome_message) + " $it!"
        }
        retroVM.getRetros()
        retroVM.retroList.observe(viewLifecycleOwner) { list ->
            loading_view.isVisible = false
            empty_view.isVisible = list.isNullOrEmpty()
            adapter = PreviewRetroAdapter(list.map { RetroPreview(RetroSubTypes.valueOf(it.subtype), it.title,
                it.date, it.username) })
            retro_list.isVisible = !list.isNullOrEmpty()

            with(retro_list) {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@MainFragment.adapter
            }
        }
        persistedVM.isScrumMaster().observe(viewLifecycleOwner) {
            add_team_button.setOnClickListener { navigateAddTeam() }
            label_add_team.setOnClickListener { navigateAddTeam() }
            icon_add_team.setOnClickListener { navigateAddTeam() }
            setVisibilityViews(it.second, listOf(add_team_button, label_add_team, icon_add_team))
        }
        floating_action_button.setOnClickListener {
            findNavController().navigate(R.id.selectionTypeFragment)
        }

        help_icon.setOnClickListener {
            showDialog(R.string.title_help, R.string.message_help, R.string.accept_button) {}
        }
    }

    private fun navigateAddTeam() {
        findNavController().navigate(R.id.addTeamFragment)
    }


}

class PreviewRetroAdapter(val items: List<RetroPreview>) : RecyclerView.Adapter<PreviewRetroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.view_retro_preview_card))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: RetroPreview) = with(itemView) {
            title_retro.text = data.title
            date_retro.text = data.date
            author_retro.text = getString(R.string.author_retro) + data.author
            icon_retro.setImageDrawable(resources.getDrawable(
                when(data.type) {
                    RetroSubTypes.STARFISH -> R.drawable.ic_starfish
                    RetroSubTypes.SAILBOAT -> R.drawable.ic_sailboat
                    RetroSubTypes.FAST_AND_FURIOUS -> R.drawable.ic_fast
                    RetroSubTypes.THREE_LITTLE_PIGS -> R.drawable.ic_pig
                    RetroSubTypes.SEMAPHORE -> R.drawable.ic_sempahore
                    RetroSubTypes.FOUR_L -> R.drawable.ic_fourls
                }
            ,null))
        }
    }
}

data class RetroPreview(val type: RetroSubTypes, val title: String, val date: String, val author: String)

