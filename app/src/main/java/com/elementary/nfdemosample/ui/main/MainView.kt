package com.elementary.nfdemosample.ui.main

interface MainView {

    fun showProgress()
    fun hideProgress()
    fun attachHomeFragment()
    fun attachMessageFragment()
    fun attachProfileFragment()
    fun attachNotificationFragment()
    fun attachSettingsFragment()
    fun apiSuccess(message: String)
    fun apiError(message: String)
}