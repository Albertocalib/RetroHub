package com.example.retrohub.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.retrohub.R
import com.example.retrohub.model_view.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login_view.*
import org.koin.core.KoinComponent

class LoginFragment : Fragment(), KoinComponent {

    private val vm by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LayoutInflater.from(context).inflate(R.layout.fragment_login_view,container,false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: subscribe to view model
        vm.state.observe(viewLifecycleOwner, Observer { Toast.makeText(context,"holi",Toast.LENGTH_SHORT).show() })
        button_login.setOnClickListener {
            vm.getLogin(user_name_input.text.toString(),password_input.text.toString())
        }
    }
}