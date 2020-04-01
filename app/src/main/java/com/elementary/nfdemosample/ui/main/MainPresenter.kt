package com.elementary.nfdemosample.ui.main

import android.content.Context


class MainPresenter(var mainView: MainView?) :
    MainInteractor.onMainViewListner {


    override fun onHomeFragmentAttach() {
        if (mainView != null) {
            mainView!!.attachHomeFragment()

        }
    }

    override fun onProfileFragmentAttach() {
        if (mainView != null) {
            mainView!!.attachProfileFragment()

        }
    }

    override fun onNotificationFragmentAttach() {
        if (mainView != null) {
            mainView!!.attachNotificationFragment()

        }
    }
    override fun onSettingsFragmentAttach() {
        if (mainView != null) {
            mainView!!.attachSettingsFragment()

        }
    }
    override fun onMessageFragmentAttach() {
        if (mainView != null) {
            mainView!!.attachMessageFragment()

        }
    }
    override fun onSuccess(message: String) {

    }

    override fun onError(message: String) {

    }

}