package com.example.retrohub

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.observe
import com.example.retrohub.model_view.LoginState
import com.example.retrohub.model_view.LoginViewModel
import com.example.retrohub.model_view.RegisterViewModel
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

@RunWith(MockitoJUnitRunner::class)
class RegisterUnitTest : LifecycleOwner {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var userRepository: UserRepository
    private val lifecycle = LifecycleRegistry(this)

    override fun getLifecycle() = lifecycle

    @Test
    fun loginOnFailure(){
        val vm = RegisterViewModel(userRepository)
        vm.state.observe(this, ::onFailure)
        vm.onFailure(mock(Call::class.java) as Call<Boolean>,IOException())
    }
    private fun onFailure(state: RegisterViewModel.RegisterState) { assertEquals(RegisterViewModel.RegisterState.SERVICE_ERROR,state) }


    @Test
    fun loginOnSuccess(){
        val vm = RegisterViewModel(userRepository)
        vm.state.observe(this, ::onSuccess)
        vm.onResponse(mock(Call::class.java) as Call<Boolean>, Response.success(true))
    }
    private fun onSuccess(state: RegisterViewModel.RegisterState) { assertEquals(RegisterViewModel.RegisterState.SUCCESS,state) }


    @Test
    fun loginOnError(){
        val vm = RegisterViewModel(userRepository)
        vm.state.observe(this, ::onError)
        vm.onResponse(mock(Call::class.java) as Call<Boolean>, Response.error(406, EMPTY_RESPONSE))
    }
    private fun onError(state: RegisterViewModel.RegisterState) { assertEquals(RegisterViewModel.RegisterState.AUTHENTICATION_ERROR,state) }




}
