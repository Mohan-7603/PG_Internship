package com.example.pg_notification_app

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.pg_notification_app.HelperClass
import com.example.pg_notification_app.NotificationListFragment
import com.example.pg_notification_app.NotificationModel
import com.example.pg_notification_app.R
import com.example.pg_notification_app.channelID
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class sndNotificationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_snd_notification, container, false)

        val sendNotificationButton = view.findViewById<Button>(R.id.message_button)
        sendNotificationButton.setOnClickListener {
            val titleEditText = view.findViewById<EditText>(R.id.title_content)
            val messageEditText = view.findViewById<EditText>(R.id.message_content)
            val title = titleEditText.text.toString().trim()
            val message = messageEditText.text.toString().trim()

            val currentUser = mAuth.currentUser
            val currentUsername = getUsername(currentUser.toString())
            if (title.isNotEmpty() && message.isNotEmpty()) {
                if (currentUser != null) {
                    val currentUserId = mAuth.currentUser?.uid ?: ""
                    val senderUsername = mAuth.currentUser?.displayName ?: ""
                    val notification = NotificationModel(title, message,currentUserId,senderUsername)
//                    mDatabase.child("sentNotifications").child(mAuth.currentUser?.uid ?: "").push().setValue(notification)


                    val reference: DatabaseReference =
                        FirebaseDatabase.getInstance().getReference("users")
                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val deviceTokens = mutableListOf<String>()
                            for (snapshot in dataSnapshot.children) {
                                val helperClass = snapshot.getValue(HelperClass::class.java)
                                helperClass?.let {
                                    deviceTokens.add(it.deviceToken)
                                }
                            }
                            // Send the notification to the devices of other users
                            sendNotification(title, message, deviceTokens, false)

                            // Add the notification to the `sentNotifications` node under the current user's UID
                            sendNotification(title, message, mutableListOf(), true)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error
                        }
                    })

                    titleEditText.setText("")
                    messageEditText.setText("")
                }
            }else {
                val message = if (title.isEmpty()) {
                    "Please fill in the title to continue."
                } else {
                    "Please enter the message to continue."
                }
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun getUsername(uid: String): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.displayName
    }

    private fun sendNotification(
        title: String,
        message: String,
        deviceTokens: MutableList<String>,
        isSentByCurrentUser: Boolean
    ) {
        // Create an intent for the notification tap action
        val intent = Intent(activity, NotificationListFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(
            activity,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification using the message entered by the user
        val notificationBuilder = NotificationCompat.Builder(requireContext(), channelID)
            .setSmallIcon(R.drawable.pg)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)

        // Send the notification
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

        if (isSentByCurrentUser) {
            // Add the notification to the `sentNotifications` node under the current user's UID
            mDatabase.child("sentNotifications").child(mAuth.currentUser!!.uid).push()
                .setValue(NotificationModel(title, message,mAuth.currentUser?.uid ?: "", mAuth.currentUser!!.displayName ?: ""))
        } else {
            // Add the notification to the `notifications` node
            mDatabase.child("notifications").push()
                .setValue(NotificationModel(title, message,mAuth.currentUser?.uid ?: "", mAuth.currentUser!!.displayName ?: ""))
        }

        // Clear the text after the notification is sent
        view?.findViewById<EditText>(R.id.title_content)?.setText("")
        view?.findViewById<EditText>(R.id.message_content)?.setText("")
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            sndNotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}