package com.example.retrohub

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.retrohub.extensions.getColor
import com.example.retrohub.extensions.hideKeyboard
import com.example.retrohub.model_view.LoginViewModel
import com.example.retrohub.model_view.RegisterViewModel
import com.example.retrohub.repository.UserRepository
import com.example.retrohub.service.UserService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        fun shortlyToast() : Boolean {
            Toast.makeText(applicationContext,R.string.work_in_progress,Toast.LENGTH_LONG).show()
            return true
        }

        return when (item.itemId) {
            R.id.action_customize, R.id.action_discover,R.id.action_help,
            R.id.action_profile,R.id.action_my_retrospectives -> shortlyToast()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startKoin()  = startKoin {
            androidContext(this@MainActivity)
            modules(listOf(retrofitModule, serviceModule, repositoryModule, vmModule/*, viewModule*/))
    }


    val repositoryModule = module {
        single { UserRepository(get()) }
    }

    val vmModule = module {
        viewModel { LoginViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
    }

    val serviceModule = module {
        fun provideUseApi(retrofit: Retrofit): UserService {
            return retrofit.create(UserService::class.java)
        }
        single { provideUseApi(get()) }
    }

    val retrofitModule = module {

        fun provideGson(): Gson {
            return GsonBuilder().setLenient().create()
        }

        fun provideHttpClient(): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()

            return okHttpClientBuilder.build()
        }

        fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.136:8081")
                .addConverterFactory(GsonConverterFactory.create(factory))
                .client(client)
                .build()
        }

        single { provideGson() }
        single { provideHttpClient() }
        single { provideRetrofit(get(), get()) }
    }

    abstract class RetroHubFragment(@LayoutRes private val layoutResId: Int) : Fragment(layoutResId){
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            view.setOnClickListener { hideKeyboard() }
            requireActivity().toolbar.isVisible = true
            if (Build.VERSION.SDK_INT >= 21) {
                val window = requireActivity().window
                window.statusBarColor = getColor(R.color.colorPrimary)
            }
        }
    }


}
