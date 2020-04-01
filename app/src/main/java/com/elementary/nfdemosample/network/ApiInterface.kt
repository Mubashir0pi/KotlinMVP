package com.elementary.nfdemosample.network

import com.elementary.nfdemosample.models.GeneralResponseData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

//Author Muhammad Mubashir 10/30/2018


interface ApiInterface {

    @POST("user/login")
    fun callLoginUser(@Body params: JsonObject): Call<GeneralResponseData>

    @POST("user/register")
    fun callRegisterUser(@Body params: JsonObject): Call<GeneralResponseData>

}