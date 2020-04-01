package com.elementary.nfdemosample.ui.main

import android.os.Bundle
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.elementary.nfdemosample.R
import com.elementary.nfdemosample.ui.home.HomeFragment
import com.elementary.nfdemosample.ui.notifiction.NotificationFragment
import com.elementary.nfdemosample.ui.profile.ProfileFragment
import com.elementary.nfdemosample.ui.settings.SettingsFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation


//Author Muhammad Mubashir 10/30/2018

class MainActivity : AppCompatActivity(), MainView {
    companion object {
        private const val ID_HOME = 1
        private const val ID_MESSAGE = 2
        private const val ID_NOTIFICATION = 4
        private const val ID_SETTINGS = 5
        private const val ID_PROFILE = 3
    }

    private lateinit var mContext: AppCompatActivity
    internal lateinit var bottomNavigation: MeowBottomNavigation
    private lateinit var presenter: MainPresenter
    private lateinit var toolbar: Toolbar
    private lateinit var toolBarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mContext = this
        presenter = MainPresenter(this)
        initializedToolBar()
        initializedViews()

    }

    private fun initializedToolBar() {
        toolbar = findViewById(R.id.toolBar)
        toolBarTitle = toolbar.findViewById(R.id.title)

    }

    private fun initializedViews() {
        bottomNavigation = this.findViewById(R.id.bottomNavigation)
        bottomNavigation.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_homepage))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_chat))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_user))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_notification))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_SETTINGS, R.drawable.ic_settings))
        bottomNavigation.setCount(ID_NOTIFICATION, "3")
        bottomNavigation.show(1, true)
        presenter.onHomeFragmentAttach()
        toolBarTitle.text = resources.getString(R.string.home_fragment)

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                ID_HOME -> {
                    presenter.onHomeFragmentAttach()
                    toolBarTitle.text = resources.getString(R.string.home_fragment)
                }
                ID_MESSAGE -> {
                    presenter.onMessageFragmentAttach()
                    toolBarTitle.text = resources.getString(R.string.message_fragment)
                }
                ID_PROFILE -> {
                    presenter.onProfileFragmentAttach()
                    toolBarTitle.text = resources.getString(R.string.profile_fragment)
                }
                ID_NOTIFICATION -> {
                    presenter.onNotificationFragmentAttach()
                    toolBarTitle.text = resources.getString(R.string.notification_fragment)

                }
                ID_SETTINGS -> {
                    presenter.onSettingsFragmentAttach()
                    toolBarTitle.text = resources.getString(R.string.settings_fragment)
                }
                else -> {
                    presenter.onHomeFragmentAttach()
                    toolBarTitle.text = resources.getString(R.string.home_fragment)
                }
            }

        }


    }


    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun apiSuccess(message: String) {
    }

    override fun apiError(message: String) {
    }

    override fun attachHomeFragment() {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.rlContainer, homeFragment).commit()

    }

    override fun attachProfileFragment() {
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.rlContainer, profileFragment).commit()
    }

    override fun attachMessageFragment() {

    }

    override fun attachSettingsFragment() {
        val settingsFragment = SettingsFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.rlContainer, settingsFragment).commit()
    }

    override fun attachNotificationFragment() {
        val notificationFragment = NotificationFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.rlContainer, notificationFragment).commit()
    }

}
