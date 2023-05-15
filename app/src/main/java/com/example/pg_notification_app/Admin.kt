package com.example.pg_notification_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pg_notification_app.AboutFragment
import com.example.pg_notification_app.HomeFragment
import com.example.pg_notification_app.NotificationListFragment
import com.example.pg_notification_app.R
import com.example.pg_notification_app.SettingsFragment
import com.example.pg_notification_app.sndNotificationFragment
import com.google.android.material.navigation.NavigationView
import com.example.pg_notification_app.UserManagement



class Admin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create the notification channel
        createNotificationChannel()

        setContentView(R.layout.activity_admin)

        drawerLayout = findViewById<DrawerLayout>(R.id.admin_drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.admin_toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.admin_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav1_home)
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav1_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav1_userManagement-> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserManagement()).commit()
            R.id.nav1_notlist -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotificationListFragment()).commit()
            R.id.nav1_snd -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, sndNotificationFragment()).commit()
            R.id.nav1_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment()).commit()
            R.id.nav1_about -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutFragment()).commit()
        }
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Notification permission is granted
        } else {
            // Request notification permission
            val intent = Intent().apply {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("android.provider.extra.APP_PACKAGE", packageName)
            }
            startActivity(intent)
        }

        // Logout button code
        if (item.itemId == R.id.nav1_logout) {
            // Clear any user session or data
            // ...

            // Start the login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

            // Show a Toast message indicating that the user has logged out
            Toast.makeText(this, "You have been logged out successfully!", Toast.LENGTH_SHORT).show()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

