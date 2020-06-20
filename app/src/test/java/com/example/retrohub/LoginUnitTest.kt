package com.example.retrohub

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.observe
import com.example.retrohub.model_view.LoginState
import com.example.retrohub.model_view.LoginViewModel
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.service.UserService
import okhttp3.ResponseBody
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


private const val FAKE_STRING="1234"

@RunWith(MockitoJUnitRunner::class)
class LoginUnitTest : LifecycleOwner {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var userRepository: UserRepository
    private val lifecycle = LifecycleRegistry(this)

    override fun getLifecycle() = lifecycle

    @Test
    fun login_on_failure(){
        val vm = LoginViewModel(userRepository)
        vm.state.observe(this, ::onError)
        vm.onFailure(mock(Call::class.java) as Call<Boolean>,IOException())
    }
    private fun onError(state: LoginState?) { assertEquals(LoginState.ERROR,state) }


    @Test
    fun login_on_success(){
        val vm = LoginViewModel(userRepository)
        vm.state.observe(this, ::onSuccess)
        vm.onResponse(mock(Call::class.java) as Call<Boolean>, Response.success(true))
    }
    private fun onSuccess(state: LoginState) { assertEquals(LoginState.SUCCESS,state) }


    @Test
    fun login_on_error(){
        val vm = LoginViewModel(userRepository)
        vm.state.observe(this, ::onError)
        vm.onResponse(mock(Call::class.java) as Call<Boolean>, Response.error(405, EMPTY_RESPONSE))
    }

    @Test
    fun login_with_empty_credentials() {
        val vm = LoginViewModel(userRepository)
        vm.getLogin("","")
        onError(vm.state.value)
    }

    @Test
    fun login_without_username() {
        val vm = LoginViewModel(userRepository)
        vm.getLogin("", FAKE_STRING)
        onError(vm.state.value)
    }

    @Test
    fun login_without_password() {
        val vm = LoginViewModel(userRepository)
        vm.getLogin(FAKE_STRING, "")
        onError(vm.state.value)
    }



}
