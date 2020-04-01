package com.elementary.nfdemosample.ui.login

import android.content.Context


class LoginPresenter(var loginView: LoginView?, val loginInteractor: LoginInteractor) :
        LoginInteractor.OnLoginFinishedListener {


    fun validateCredentials(mContext: Context, email: String, password: String) {
        if (loginView != null) {
            loginView!!.showProgress()
        }

        loginInteractor.login(mContext, email, password, this)
    }

    override fun onEmailError() {
        if (loginView != null) {
            loginView!!.setEmailError();
            loginView!!.hideProgress();
        }
    }

    override fun onPasswordError() {
        if (loginView != null) {
            loginView!!.setPasswordError();
            loginView!!.hideProgress();
        }

    }

    override fun onSuccess(message: String) {
        if (loginView != null) {
            loginView!!.apiSuccess(message);
            loginView!!.hideProgress();

        }
    }

    override fun onError(message: String) {
        if (loginView != null) {
            loginView!!.apiError(message);
            loginView!!.hideProgress();

        }
    }

    fun onDestroy() {
        loginView = null
    }

}