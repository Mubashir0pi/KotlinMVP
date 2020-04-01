package com.elementary.nfdemosample.network


import com.elementary.nfdemosample.models.GeneralResponseData
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

//Author Muhammad Mubashir 10/30/2018

class ApiClient {
    private val apiInterface: ApiInterface
    private val gson: Gson


    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val ongoing = chain.request().newBuilder()
                    ongoing.addHeader("Accept", "application/json")

                    chain.proceed(ongoing.build())
                }
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

    init {
        this.gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        retrofit = Retrofit.Builder()
                .baseUrl(ApiUrls.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        this.apiInterface = retrofit!!.create(ApiInterface::class.java!!)
    }

    companion object {

        private var retrofit: Retrofit? = null
        var instance: ApiClient? = null
            get() {
                if (field == null) {
                    instance = ApiClient()
                }
                return field
            }
    }

    fun callLoginUser(@Body params: JsonObject, callback: Callback<GeneralResponseData>) {
        val call = this.apiInterface.callLoginUser(params)
        call.enqueue(callback)
    }
    fun callRegisterUser(@Body params: JsonObject, callback: Callback<GeneralResponseData>) {
        val call = this.apiInterface.callRegisterUser(params)
        call.enqueue(callback)
    }
}
