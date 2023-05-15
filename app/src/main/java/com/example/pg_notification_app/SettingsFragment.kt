package com.example.pg_notification_app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Get the logout TextView and set an onClickListener to start the Login activity
        val arrowButton : ImageView = view.findViewById(R.id.back_button)
        arrowButton.setOnClickListener {
            val fragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val logoutTextView: RelativeLayout = view.findViewById(R.id.logout_textview)
        logoutTextView.setOnClickListener {
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
            activity?.finish()
        }
        // Get the night mode switch and set an onCheckedChangedListener to change the color scheme
        val nightModeSwitch: SwitchCompat = view.findViewById(R.id.night_mode_switch)
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return view
    }

}
