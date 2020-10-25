package com.example.parkingfinder.models

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingfinder.MapsActivity
import com.example.parkingfinder.api.RetrofitClient
import com.example.parkingfinder.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

     val user = MutableLiveData<String>()
    fun loginRequest(username:String,password:String, context:Context){
        RetrofitClient.instance.userLogin( username, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    user.postValue(username)

                    response.body()?.let { responseLogin ->
                        SharedPrefManager.getInstance(context)
                            .saveUser(responseLogin)
                    }
                }
            })
    }
}