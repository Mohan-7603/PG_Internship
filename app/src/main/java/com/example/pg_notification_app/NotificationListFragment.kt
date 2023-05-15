package com.example.pg_notification_app

import android.app.Notification
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//class NotificationListFragment : Fragment() {
//
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var mDatabase: DatabaseReference
//    private lateinit var notificationsRecyclerView: RecyclerView
//    private lateinit var notificationsAdapter: NotificationsAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mAuth = FirebaseAuth.getInstance()
//        mDatabase = FirebaseDatabase.getInstance().reference
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_notification_list, container, false)
//        notificationsRecyclerView = view.findViewById(R.id.notification_list_view)
//        notificationsAdapter = NotificationsAdapter(emptyList())
//        notificationsRecyclerView.adapter = notificationsAdapter
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val notifications = mutableListOf<String>()
//        val currentUser = mAuth.currentUser
//        if (currentUser != null) {
//            val userId = currentUser.uid
//            mDatabase.child("sentNotifications").child(userId).addValueEventListener(object :
//                ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    notifications.clear()
//                    for (childSnapshot in snapshot.children) {
//                        notifications.add(childSnapshot.getValue(String::class.java)!!)
//                    }
//                    updateNotifications(notifications)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle the error
//                }
//            })
//        }
//    }
//
//    private fun updateNotifications(notifications: List<String>) {
//        notificationsAdapter.notifications = notifications
//        notificationsAdapter.notifyDataSetChanged()
//    }
//}


class NotificationListFragment : Fragment(), NotificationsAdapter.OnNotificationClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var isSentByCurrentUser: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        isSentByCurrentUser = mAuth.currentUser != null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentUser = mAuth.currentUser
        val view = inflater.inflate(R.layout.fragment_notification_list, container, false)
        notificationsRecyclerView = view.findViewById(R.id.notification_list_view)
        notificationsAdapter = NotificationsAdapter(emptyList(), currentUser, this)
        notificationsRecyclerView.adapter = notificationsAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = mAuth.currentUser
        val notificationsRef = if (isSentByCurrentUser) {
            mDatabase.child("sentNotifications").child(mAuth.currentUser!!.uid)
        } else {
            mDatabase.child("notifications")
        }
        notificationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notifications = mutableListOf<NotificationModel>()
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(NotificationModel::class.java)
                    notification?.let {
                        notifications.add(it)
                    }
                }
                notifications.reverse()

                // Initialize the adapter and attach it to the RecyclerView
                notificationsAdapter = NotificationsAdapter(notifications, currentUser, this@NotificationListFragment)
                notificationsRecyclerView.adapter = notificationsAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
        notificationsAdapter.setOnNotificationClickListener(this)
    }

    override fun onNotificationClick(notification: NotificationModel) {
            val dialogFragment = NotificationDetailsDialogFragment.newInstance(notification.title ?: "", notification.message ?: "")
            dialogFragment.show(childFragmentManager, NotificationDetailsDialogFragment.TAG)
        }


//    private fun updateNotifications(notifications: List<NotificationModel>) {
//        notificationsAdapter.notifications = notifications
//        notificationsAdapter.notifyDataSetChanged()
//    }

    companion object {
        private const val TAG = "NotificationListFragment"
    }
}

