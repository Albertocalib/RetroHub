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
import kotlinx.android.synthetic.main.view_card_type.view.*
import org.koin.android.ext.android.inject


class SelectionTypeFragment : MainActivity.RetroHubFragment(R.layout.fragment_selection_type), MyAdapter.ViewHolder.Action {

    private lateinit var subtypeSelected: RetroSubTypes
    private lateinit var adapter: MyAdapter
    private val viewModel: PersistedRetroViewModel by inject()

    private val typeSelected: RetroType
        get() = if(release_option.isChecked) RetroType.RELEASE else RetroType.SPRINT

    override fun getToolbarTitle() = "Selección de tipo"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideLoading()
        with(subtypes_list){
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = MyAdapter(RetroSubTypes.values(),this@SelectionTypeFragment).also { this@SelectionTypeFragment.adapter = it }
        }
        viewModel.state.observe(viewLifecycleOwner, ::setState)
        next_step.setOnClickListener { viewModel.saveRetro(typeSelected.title,subtypeSelected.title) }
    }

    private fun setState(state: State){
        when(state){
            State.READY -> hideLoading()
            State.SAVING -> showLoading()
            State.SAVED -> Toast.makeText(requireContext(),"GUARDADO", Toast.LENGTH_SHORT).show()
            State.ERROR -> showDialog(R.string.error_message_title,R.string.error_message_service,R.string.accept_button){ }
        }
    }

    private fun showLoading(){
        setVisibilityViews(false, listOf(label_subtype, label_type, divider, radioGroup, subtypes_list, next_step))
        loading_view.isVisible = true
    }

    private fun hideLoading(){
        setVisibilityViews(true, listOf(label_subtype,label_type,divider,radioGroup,subtypes_list))
        loading_view.isVisible = false
    }

    override fun onItemSelected(item: RetroSubTypes) {
        if(::subtypeSelected.isInitialized) adapter.notifyItemChanged(RetroSubTypes.values().indexOf(subtypeSelected))
        subtypeSelected = item
    }
}

class MyAdapter(val items: Array<RetroSubTypes>, val listener: ViewHolder.Action) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.view_card_type))

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(items[position], listener)

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        interface Action{
            fun onItemSelected(item: RetroSubTypes)
        }

        fun bind(item: RetroSubTypes, listener: Action) = with(itemView) {
            name.text = getString(item.res)
            fragment_container_selection_type.isChecked = isSelected
            fragment_container_selection_type.setOnClickListener {
                if(!fragment_container_selection_type.isChecked) {
                    fragment_container_selection_type.isChecked = true
                    listener.onItemSelected(item)
                }
            }
        }
    }
}