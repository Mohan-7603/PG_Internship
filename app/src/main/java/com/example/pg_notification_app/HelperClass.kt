package com.example.pg_notification_app

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HelperClass(val username: String, val email: String, val password: String, val uid: String, val deviceToken: String,
                       var userType: String) :
    Parcelable {
    constructor() : this("", "","","","","")
}
