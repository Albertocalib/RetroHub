package com.example.retrohub.view

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.showDialog
import com.example.retrohub.model_view.RegisterViewModel
import com.example.retrohub.view.mobile.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_register_view.*
import kotlinx.android.synthetic.main.fragment_register_view.view.*
import org.koin.android.ext.android.inject

class RegisterFragment : MainActivity.RetroHubFragment(R.layout.fragment_register_view) {

    private lateinit var user : User
    private var validated = false

    private val vm: RegisterViewModel by inject()

    override fun getToolbarTitle() = "Crear cuenta"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar.hideOverflowMenu()
        requireActivity().toolbar.title = getString(R.string.register_toolbar)
        save_button.setOnClickListener {
            validate()
        }
        vm.state.observe(viewLifecycleOwner, ::onStateChanged)
    }

    private fun onStateChanged(state: RegisterViewModel.RegisterState) =
        when (state) {
            RegisterViewModel.RegisterState.AUTHENTICATION_ERROR -> showDialog(
                R.string.error_message_title,
                R.string.error_message_authentication,
                R.string.accept_button
            ) {
                user_name_input.error = getString(R.string.invalid_username)
            }
            RegisterViewModel.RegisterState.SERVICE_ERROR -> showDialog(
                R.string.error_message_title,
                R.string.error_message_service
            )
            { findNavController().navigate(R.id.loginFragment) }
            RegisterViewModel.RegisterState.SUCCESS -> findNavController().navigate(R.id.loginFragment)
        }

    private fun validate() {
        validated = true
        user = User(
            validText(name_input) { true }.also { validated = it != "" },
            validText(lastname_input) { true }.also { validated = it != "" },
            validText(user_name_input) { true }.also { validated = it != "" },
            validText(document_input, pattern = ::isDocumentValid).also { validated = it != "" },
            getString(if (nie_option.isChecked) R.string.nie else R.string.nif).also { validated = it != "" },
            validText(password_input) { true }.also { validated = it != "" },
            validText(email_input, true, ::isEmailValid)
        )
        //TODO: add loading
        if (validated) vm.createAccount(user)
    }

    private fun isEmailValid(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email_input.editText!!.text.toString()).matches()

    private fun isDocumentValid() =
        "([a-z]|[A-Z]|[0-9])[0-9]{7}([a-z]|[A-Z]|[0-9])".toRegex()
            .matches(document_input.editText!!.text.toString())

    private fun validText(text: TextInputLayout, optional: Boolean = false, pattern: () -> Boolean) =
        if (text.editText!!.text.isNullOrBlank()) {
            validated = optional
            text.error = if (!optional)  getString(R.string.fill_field) else null
            ""
        } else if (!pattern.invoke()) {
            validated = false
            text.error = getString(R.string.fill_field_incorrect)
            ""
        } else {
            text.error = null
            text.editText!!.text.toString()
        }


}

