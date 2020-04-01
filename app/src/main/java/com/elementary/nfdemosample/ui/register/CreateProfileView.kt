package com.elementary.nfdemosample.ui.register

interface CreateProfileView {

    fun showProgress()
    fun hideProgress()
    fun setEmailError()
    fun setPasswordError()
    fun setGenderError()
    fun setNameError()
    fun setDOBError()
    fun apiSuccess(message: String)
    fun apiError(message: String)
}