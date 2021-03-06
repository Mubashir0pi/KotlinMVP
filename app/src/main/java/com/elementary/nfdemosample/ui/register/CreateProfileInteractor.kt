package com.elementary.nfdemosample.ui.register

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.elementary.nfdemosample.R
import com.elementary.nfdemosample.models.GeneralResponseData
import com.elementary.nfdemosample.network.ApiClient
import com.elementary.nfdemosample.utils.AppUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class CreateProfileInteractor {

    interface OnRegisterFinishedListener {
        fun onEmailError()

        fun onPasswordError()

        fun onNameError()

        fun onDobError()

        fun onGenderError()

        fun onSuccess(message: String)

        fun onError(message: String)
    }


    fun login(mContext: Context, email:String,name:String,password:String,dob:String,gender:String, listener: OnRegisterFinishedListener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        Handler().postDelayed(new@{
            when {
                TextUtils.isEmpty(name) -> listener.onNameError()
                TextUtils.isEmpty(email) -> listener.onEmailError()
                TextUtils.isEmpty(password) -> listener.onPasswordError()
                TextUtils.isEmpty(dob) -> listener.onDobError()
                TextUtils.isEmpty(gender) -> listener.onGenderError()
                else -> submitRegisterRequest(mContext, email, name,password,dob,gender, listener)

            }


        }, 1000)
    }

    private fun submitRegisterRequest(mContext: Context, email:String,name:String,password:String,dob:String,gender:String, listener: OnRegisterFinishedListener) {


        if (!AppUtils.isOnline(mContext)) {
            listener.onError(mContext.getString(R.string.internet_not_responding))
        }
        try {
            val paramObject = JsonObject()
            paramObject.addProperty("name", name)
            paramObject.addProperty("email", email)
            paramObject.addProperty("password", password)
            paramObject.addProperty("dob", password)

            callRegisterRequest(listener, mContext, paramObject)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun callRegisterRequest(listener: OnRegisterFinishedListener, mContext: Context, params: JsonObject) {
        val apiClient = ApiClient.instance

        apiClient!!.callRegisterUser(params, object : Callback<GeneralResponseData> {
            override fun onResponse(call: Call<GeneralResponseData>,
                                    response: Response<GeneralResponseData>) {


                if (response.isSuccessful) {
                    if (response.body()?.status!!) {
                        listener.onSuccess(response.body()!!.message!!)
                    } else {
                        listener.onError(response.body()!!.message!!)
                    }

                } else {
                    if (response.code() == 404)
                        listener.onError(mContext.getString(R.string.http_error))


                    if (response.code() == 500)
                        listener.onError(mContext.getString(R.string.server_error))

                }
            }

            override fun onFailure(call: Call<GeneralResponseData>, t: Throwable) {


                if (t is IOException)
                    listener.onError(mContext.getString(R.string.api_not_found))
                else if (t is SocketTimeoutException)
                    listener.onError(mContext.getString(R.string.connection_time_out))
                else
                    listener.onError(t.getLocalizedMessage())

            }
        })
    }

}