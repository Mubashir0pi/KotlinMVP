package com.elementary.nfdemosample.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elementary.nfdemosample.R
import com.elementary.nfdemosample.ui.login.LoginActivity
import com.elementary.nfdemosample.ui.main.MainActivity
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


//Author Muhammad Mubashir 10/30/2018

class SplashActivity : AppCompatActivity() {


    private lateinit var mContext: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        mContext = this

            Executors.newSingleThreadScheduledExecutor().schedule({
                openNextActivity()
            }, 3, TimeUnit.SECONDS)




    }


     fun openNextActivity() {
        val myIntent = Intent(mContext, MainActivity::class.java)
        startActivity(myIntent)
        finish()
    }


}
