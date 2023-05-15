package com.example.pg_notification_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserListAdapter(private val userList: List<HelperClass>) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.user_name)
//        val userTypeLinearLayout: LinearLayout = view.findViewById(R.id.user_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userNameTextView.text = user.username
        val userTypeRadioGroup = holder.itemView.findViewById<RadioGroup>(R.id.user_type)
        val userCheckBox: RadioButton = userTypeRadioGroup.findViewById(R.id.user_type_user)
        val adminCheckBox: RadioButton = userTypeRadioGroup.findViewById(R.id.user_type_admin)
        val senderCheckBox: RadioButton = userTypeRadioGroup.findViewById(R.id.user_type_sender)


        when (user.userType) {
            "Admin" -> adminCheckBox.isChecked = true
            "User" -> userCheckBox.isChecked = true
            "Sender" -> senderCheckBox.isChecked = true
        }
//        // Get a reference to the Firebase database
//        val databaseReference = FirebaseDatabase.getInstance().reference
//
//// Assuming "users" is the name of the node in the database where user data is stored
//        val usersReference = databaseReference.child("users")
//
//// Update the user's type in the database when the checkbox is clicked
//        userCheckBox.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Update the user's type to "User" in the database
//                val currentUser = FirebaseAuth.getInstance().currentUser
//                currentUser?.let { user ->
//                    val userId = user.uid
//                    usersReference.child(userId).child("userType").setValue("User")
//                }
//            }
//        }
//
//        adminCheckBox.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Update the user's type to "User" in the database
//                val currentUser = FirebaseAuth.getInstance().currentUser
//                currentUser?.let { user ->
//                    val userId = user.uid
//                    usersReference.child(userId).child("userType").setValue("Admin")
//                }
//            }
//        }
//
//        senderCheckBox.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Update the user's type to "Sender" in the database
//                val currentUser = FirebaseAuth.getInstance().currentUser
//                currentUser?.let { user ->
//                    val userId = user.uid
//                    usersReference.child(userId).child("userType").setValue("Sender")
//                }
//            }
//        }
//
//
//    }
////    private fun updateUserTypeInDatabase(userId: String, userType: String) {
////        val databaseReference = FirebaseDatabase.getInstance().reference
////        val userRef = databaseReference.child("users").child(userId)
////        userRef.child("userType").setValue(userType)
////    }

    }
        override fun getItemCount() = userList.size

}
