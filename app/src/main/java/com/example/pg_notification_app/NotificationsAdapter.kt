package com.example.pg_notification_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser

class NotificationsAdapter(
    var notifications: List<NotificationModel>,
    private val currentUser: FirebaseUser?,
    private var listener: OnNotificationClickListener
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    fun setOnNotificationClickListener(listener: OnNotificationClickListener) {
        this.listener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val notification = notifications[position]
                    listener.onNotificationClick(notification)
                }
            }
        }
        private val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        private val messageTextView: TextView = itemView.findViewById(R.id.message_text_view)

        fun bind(notification: NotificationModel) {
            titleTextView.text = notification.title
            messageTextView.text = notification.message
            itemView.setOnClickListener {
                listener.onNotificationClick(notification)
            }
        }
    }

    interface OnNotificationClickListener {
        fun onNotificationClick(notification: NotificationModel)
    }
}
