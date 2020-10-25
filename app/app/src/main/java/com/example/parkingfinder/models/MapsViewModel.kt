package com.example.parkingfinder.models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingfinder.api.RetrofitClient
import com.example.parkingfinder.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {

    private val username: MutableLiveData<String?> = MutableLiveData(null)

    fun parkingRequest(taken: Boolean, idUser: Int, idParking: Int, context: Context) {
        RetrofitClient.instance.greyEntered(taken = taken, idUser = idUser, idParking = idParking)
            .enqueue(object : Callback<GreyResponse> {
                override fun onFailure(call: Call<GreyResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<GreyResponse>,
                    response: Response<GreyResponse>
                ) {
                    response.body()?.let { GreyResponse ->
                        SharedPrefManager.getInstance(context)
                            .saveUserPoints(GreyResponse)
                    }

                }
            })
    }


}