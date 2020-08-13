package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.inflate
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.PersistedRetroViewModel
import com.example.retrohub.model_view.State
import com.example.retrohub.view.mobile.RetroSubTypes
import kotlinx.android.synthetic.main.fragment_fill_retro.*
import kotlinx.android.synthetic.main.fragment_fill_retro.loading_title_view
import kotlinx.android.synthetic.main.fragment_fill_retro.next_step
import kotlinx.android.synthetic.main.fragment_title.*
import kotlinx.android.synthetic.main.view_drop_down_list.view.*
import kotlinx.android.synthetic.main.view_info_retro_list.view.*
import org.koin.android.ext.android.inject


class FillRetroInfoFragment : MainActivity.RetroHubFragment(R.layout.fragment_fill_retro),
    ListAdapter.ViewHolder.Action, AddRetroInfoDialog.Action {

    override fun getToolbarTitle() = "Añadir información"

    private var info: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val viewModel: PersistedRetroViewModel by inject()
    private lateinit var adapter: ListAdapter
    private lateinit var subtype: RetroSubTypes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.type.observe(viewLifecycleOwner) { subtype ->
            subtype.fields.forEach { info[it] = mutableListOf() }
            with(list_items) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ListAdapter(
                    info.toList(),
                    this@FillRetroInfoFragment
                ).also { this@FillRetroInfoFragment.adapter = it }
            }
            add_card_button.setOnClickListener {
                newInstance(this, subtype.fields).show(parentFragmentManager, "AddRetroInfoDialog")
            }
            this.subtype = subtype
        }
        next_step.setOnClickListener {
            if (info.any { it.value.isNotEmpty() }) {
                viewModel.state.observe(viewLifecycleOwner) {
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
                            Toast.makeText(requireContext(), "Guardado", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                viewModel.updateInfoRetro(info)
            } else {
                Toast.makeText(requireContext(), R.string.retro_empty_error, Toast.LENGTH_LONG)
                    .show()
            }
        }
        viewModel.getTypeRetro()


    }

    private fun setLoadingVisibility(loading: Boolean) {
        loading_title_view.isVisible = loading
    }

    override fun onSaveClick(text: String, field: String, index: Int) {
        loading_title_view.isVisible = true
        if (index == -1) info[field]?.add(text)
        else info[field]?.set(index, text)
        adapter.notifyDataSetChanged()
        loading_title_view.isVisible = false
    }

    override fun onItemSelected(item: String, selected: Int) {
        newInstance(this, subtype.fields, selected, info[item]?.get(selected) ?: "", item)
            .show(parentFragmentManager, "AddRetroInfoDialog")
    }

    override fun deleteItem(selected: Int, subtype: String) {
        info[subtype]?.removeAt(selected)
        adapter.notifyDataSetChanged()
    }
}

typealias RetroInfo = Pair<String, MutableList<String>>

class ListAdapter(val items: List<RetroInfo>, val listener: ViewHolder.Action) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>(), SublistAdapter.ViewHolder.Action {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.view_drop_down_list))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], this)

    override fun getItemCount() = items.size

    override fun onItemSelected(selected: Int, subtype: String) =
        listener.onItemSelected(subtype, selected)

    override fun deleteItem(selected: Int, subtype: String) = listener.deleteItem(selected, subtype)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        interface Action {
            fun onItemSelected(item: String, selected: Int)
            fun deleteItem(selected: Int, subtype: String)
        }

        fun bind(item: RetroInfo, listener: SublistAdapter.ViewHolder.Action) = with(itemView) {
            icon_arrow.isSelected = true
            list.isVisible = icon_arrow.isSelected
            title_field.setOnClickListener {
                icon_arrow.isSelected = !icon_arrow.isSelected
                list.isVisible = icon_arrow.isSelected
            }
            with(list) {
                adapter = SublistAdapter(item.second, item.first, listener)
                layoutManager = LinearLayoutManager(context)
            }
            title_field.text = item.first
        }
    }
}

class SublistAdapter(
    val items: MutableList<String>,
    val field: String,
    val listener: ViewHolder.Action
) : RecyclerView.Adapter<SublistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.view_info_retro_list))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], listener, field)

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        interface Action {
            fun onItemSelected(selected: Int, subtype: String)
            fun deleteItem(selected: Int, subtype: String)
        }

        fun bind(item: String, listener: Action, subtype: String) = with(itemView) {
            description_text.text = item
            description_card.setOnClickListener {
                listener.onItemSelected(
                    adapterPosition,
                    subtype
                )
            }
            remove_icon.setOnClickListener { listener.deleteItem(adapterPosition, subtype) }
        }
    }
}
