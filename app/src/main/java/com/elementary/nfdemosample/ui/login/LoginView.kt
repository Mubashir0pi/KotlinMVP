package com.elementary.nfdemosample.ui.login

interface LoginView {

    fun showProgress()
    fun hideProgress()
    fun setEmailError()
    fun setPasswordError()
    fun apiSuccess(message: String)
    fun apiError(message: String)
}