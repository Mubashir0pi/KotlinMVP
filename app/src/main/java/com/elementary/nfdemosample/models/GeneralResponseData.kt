package com.elementary.nfdemosample.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
//Author Muhammad Mubashir 10/30/2018

class GeneralResponseData {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

}
