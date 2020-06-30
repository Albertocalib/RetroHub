package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.max
import com.example.retrohub.extensions.toLowerCaseLocale
import com.example.retrohub.model_view.PersonalAreaViewModel
import com.example.retrohub.model_view.UserDataViewModel
import com.example.retrohub.view.mobile.Retro
import com.example.retrohub.view.mobile.User
import kotlinx.android.synthetic.main.fragment_personal_area_view.*
import org.koin.android.ext.android.inject


class PersonalAreaFragment : MainActivity.RetroHubFragment(R.layout.fragment_personal_area_view) {

    val personalAreaVM: PersonalAreaViewModel by inject()
    val personalDataVM: UserDataViewModel by inject()

    override fun getToolbarTitle() = "Área personal"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading_view.isVisible = true
        personalAreaVM.numberOfRetros.observe(viewLifecycleOwner, ::setNumberOfRetros)
        personalDataVM.user.observe(viewLifecycleOwner, ::setData)
        personalAreaVM.userName.observe(viewLifecycleOwner) { s ->
            info_user_name.text = s
            personalDataVM.getDataUser(s)
        }
        personalAreaVM.retroList.observe(viewLifecycleOwner, ::setBestRetro)
        personalAreaVM.getNumberOfRetrospectives()
    }

    private fun setBestRetro(list: List<Retro>) {
        best_retro_name.isVisible = !list.isNullOrEmpty()
        label_star.isVisible = !list.isNullOrEmpty()
        link_discover.isVisible = list.isNullOrEmpty()
        icon_star_2.isVisible = !list.isNullOrEmpty()
        icon_star_1.isVisible = !list.isNullOrEmpty()
        if (list.isNullOrEmpty()) {
            link_discover.setOnClickListener {
                Toast.makeText(requireContext(),R.string.commingSoon,Toast.LENGTH_SHORT).show()
            }
        } else {
            best_retro_name.text = list.groupingBy { it.subtype }
                .eachCount()
                .maxBy { it.value }
                ?.key
        }
        loading_view.isVisible = false
    }

    private fun setData(user: User) {
        user_name.text = "${user.name} ${user.lastname}"
        document_number.text = user.documentUser
        title_document_type.text =
            getString(if (user.documentType.toLowerCaseLocale() == "nie") R.string.nie else R.string.nif)
        email_info.text = if(user.email.isNullOrEmpty()) getString(R.string.without_data) else user.email
    }

    private fun setNumberOfRetros(data: Pair<Int, Int>) {
        emtpy_group.isVisible = data.first == 0 && data.second == 0
        group_graph.isVisible = !emtpy_group.isVisible
        if(!emtpy_group.isVisible) {
            val div = maxOf(data.max(), 1)
            progress_bar_current.progress = ((data.first.toDouble() / div) * 100).toInt()
            current_progress.text = data.first.toString()
            progress_bar_past.progress = ((data.second.toDouble() / div) * 100).toInt()
            past_progress.text = data.second.toString()
        }
    }
}
