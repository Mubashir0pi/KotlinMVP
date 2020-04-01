package com.elementary.nfdemosample.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.elementary.nfdemosample.R
import com.elementary.nfdemosample.ui.main.MainActivity
import com.elementary.nfdemosample.utils.AppUtils


//Author Muhammad Mubashir 10/30/2018

class LoginActivity : AppCompatActivity(), LoginView, View.OnClickListener {


    private lateinit var mContext: AppCompatActivity
    private lateinit var progressBar: ProgressBar
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btLogin: Button
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        mContext = this
        presenter = LoginPresenter(this, LoginInteractor())
        intitilizedView()



    }

    private fun intitilizedView() {
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        btLogin = findViewById(R.id.btLogin)
        progressBar = findViewById(R.id.progressBar)
        presenter = LoginPresenter(this, LoginInteractor())
        btLogin.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btLogin -> {
                    validateCredentials()
                }
            }
        }
    }

    private fun validateCredentials() {
        presenter.validateCredentials(mContext, email.getText().toString(), password.getText().toString());
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE;

    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE;

    }

    override fun setEmailError() {
        email.error = getString(R.string.email_error);

    }

    override fun setPasswordError() {
        password.error = getString(R.string.password_error);

    }

    override fun apiSuccess(message: String) {
        AppUtils.showDropDownSuccessNotificationAndMoveToNextActivity(mContext,
                mContext.getString(R.string.alert_information),
                message, MainActivity::class.java)

    }

    override fun apiError(message: String) {
        AppUtils.showDropDownNotification(mContext,
                mContext.getString(R.string.alert_information),
                message)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
