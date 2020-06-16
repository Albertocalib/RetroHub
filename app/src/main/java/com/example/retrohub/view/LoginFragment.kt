package com.example.retrohub.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.retrohub.MainActivity
import com.example.retrohub.R
import com.example.retrohub.model_view.LoginState
import com.example.retrohub.model_view.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login_view.*
import org.koin.android.ext.android.inject

class LoginFragment : MainActivity.RetroHubFragment(R.layout.fragment_login_view) {

    private val vm: LoginViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: subscribe to view model
        vm.state.observe(viewLifecycleOwner, ::login)
        button_login.setOnClickListener {
            vm.getLogin(user_name_input.text.toString(),password_input.text.toString())
        }

    }

    private fun login(state: LoginState){
        when(state){
            LoginState.ERROR -> Toast.makeText(context,R.string.login_error_message,Toast.LENGTH_LONG).show()
            LoginState.SUCCESS -> findNavController().navigate(R.id.mainFragment)
        }
    }
}