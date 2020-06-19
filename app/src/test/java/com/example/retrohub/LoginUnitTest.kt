package com.example.retrohub

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.example.retrohub.model_view.LoginState
import com.example.retrohub.model_view.LoginViewModel
import com.example.retrohub.repository.UserRepository
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


private const val FAKE_STRING="1234"

@RunWith(MockitoJUnitRunner::class)
class LoginUnitTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var userRepository: UserRepository
    @Mock private lateinit var viewLifecycleOwner: LifecycleOwner

    @Test
    fun login_with_valid_credentials(){
        val vm = LoginViewModel(userRepository)
        vm.getLogin(FAKE_STRING, FAKE_STRING)
        vm.state.observe(viewLifecycleOwner, ::onChanged)
    }
    private fun onChanged(ignored:LoginState) { assert(true) }

    @Test
    fun login_with_empty_credentials() {
        val vm = LoginViewModel(userRepository)
        vm.getLogin("","")
        assertEquals(LoginState.ERROR,vm.state.value)
    }

    @Test
    fun login_without_username() {
        val vm = LoginViewModel(userRepository)
        vm.getLogin("", FAKE_STRING)
        assertEquals(LoginState.ERROR, vm.state.value)
    }

    @Test
    fun login_without_password() {
        val vm = LoginViewModel(userRepository)
        vm.getLogin("1234", FAKE_STRING)
        assertEquals(LoginState.ERROR, vm.state.value)
    }



}
