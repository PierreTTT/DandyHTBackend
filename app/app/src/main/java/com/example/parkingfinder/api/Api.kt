package com.example.parkingfinder.api

import com.example.parkingfinder.models.DefaultResponse
import com.example.parkingfinder.models.GreyResponse
import com.example.parkingfinder.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("createuser")
    fun createUser(
        @Field("email") email:String,
        @Field("name") name:String,
        @Field("password") password:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("username") username:String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("grey")
    fun greyEntered(
        @Field("taken") taken: Boolean,
        @Field("idUser") idUser: Int,
        @Field("idParking") idParking: Int
    ): Call<GreyResponse>
}