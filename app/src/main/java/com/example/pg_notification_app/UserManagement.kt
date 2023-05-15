package com.example.pg_notification_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class UserManagement : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_user_management, container, false)

        view.findViewById<View>(R.id.users_list_button).setOnClickListener(this)
        view.findViewById<View>(R.id.senders_list_button).setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.users_list_button -> {
                val intent = Intent(activity, UserList::class.java)
                startActivity(intent)
            }
            R.id.senders_list_button -> {
                val intent = Intent(activity, SenderList::class.java)
                startActivity(intent)
            }
        }
    }

}
