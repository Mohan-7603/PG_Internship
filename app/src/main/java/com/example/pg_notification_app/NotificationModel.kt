package com.example.pg_notification_app

import com.google.firebase.database.IgnoreExtraProperties
//
//@IgnoreExtraProperties
//class NotificationModel {
//    var title: String? = null
//    var message: String? = null
//    var senderId: String? = null   // Add this line to define the "senderId" property
//
//    constructor() {
//        // Required empty constructor for Firebase
//    }
//
//    constructor(title: String?, message: String?, receiverId: String?,senderId: String?) {
//        this.title = title
//        this.message = message
//        this.senderId = senderId
//    }
//}
data class NotificationModel(
    var title: String? = null,
    var message: String? = null,
    val senderId : String? = null,
    var senderUsername: String? = null // new field for sender's username
)


