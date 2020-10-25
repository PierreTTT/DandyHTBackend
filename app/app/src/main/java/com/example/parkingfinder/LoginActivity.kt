package com.example.parkingfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parkingfinder.api.RetrofitClient
import com.example.parkingfinder.models.LoginResponse
import com.example.parkingfinder.models.LoginViewModel
import com.example.parkingfinder.models.MapsViewModel
import com.example.parkingfinder.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel:LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        button_login.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty()) {
                editTextUsername.error = "Email required"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }


            if (password.isEmpty()) {
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            viewModel.loginRequest(username,password,applicationContext)

            val intent = Intent(this@LoginActivity, MapsActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

    }

}