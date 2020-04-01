package com.elementary.nfdemosample.ui.main

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.elementary.nfdemosample.R
import com.elementary.nfdemosample.models.GeneralResponseData
import com.elementary.nfdemosample.network.ApiClient
import com.elementary.nfdemosample.ui.login.LoginInteractor
import com.elementary.nfdemosample.utils.AppUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class MainInteractor {
    interface onMainViewListner {
        fun onHomeFragmentAttach()
        fun onProfileFragmentAttach()
        fun onMessageFragmentAttach()
        fun onSettingsFragmentAttach()
        fun onNotificationFragmentAttach()

        fun onSuccess(message: String)

        fun onError(message: String)
    }




}