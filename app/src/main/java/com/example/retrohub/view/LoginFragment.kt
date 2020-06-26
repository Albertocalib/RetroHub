package com.example.retrohub.view

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.extensions.getColor
import com.example.retrohub.extensions.getString
import com.example.retrohub.extensions.hideKeyboard
import com.example.retrohub.extensions.setVisibilityViews
import com.example.retrohub.model_view.LoginState
import com.example.retrohub.model_view.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login_view.*
import org.koin.android.ext.android.inject


class LoginFragment : MainActivity.RetroHubFragment(R.layout.fragment_login_view) {

    private val vm: LoginViewModel by inject()

    private val username: String
        get() = user_name_input.getString()

    private val password: String
        get() = password_input.getString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.statusBarColor = getColor(R.color.colorAccent)
        }
        requireActivity().toolbar.isVisible = false
        create_account.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        hideLoading()
        vm.state.observe(viewLifecycleOwner, ::login)
        button_login.setOnClickListener {
            hideKeyboard()
            var failed = false
            user_name_input.error = if (username.isBlank()) getString(R.string.fill_field).also { failed = true } else null
            password_input.error = if (password.isBlank()) getString(R.string.fill_field).also { failed = true } else null
            if (!failed) {
                showLoading()
                vm.getLogin(user_name_input.getString(), password_input.getString() )
            }
        }
    }

    private fun login(state: LoginState) {
        when (state) {
            LoginState.ERROR -> errorMessage(R.string.alert_error_service)
            LoginState.SUCCESS -> findNavController().navigate(R.id.mainFragment)
        }
    }

    private fun errorMessage(@StringRes string: Int) {
        hideLoading()
        error_message.text = getText(string)
        error_message.isVisible = true
    }

    private fun showLoading() = setLoadingVisibility(true)

    private fun hideLoading() = setLoadingVisibility(false)

    private fun setLoadingVisibility(isVisible: Boolean) {
        setVisibilityViews(
            isVisible,
            listOf(loading_text, loading_background, lottieAnimationLoading)
        )

        user_name_input.isEnabled = !isVisible
        password_input.isEnabled = !isVisible
        button_login.isClickable = !isVisible
        button_login.isEnabled = !isVisible
    }
}