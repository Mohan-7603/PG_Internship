package com.example.pg_notification_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SenderList : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var senderList: MutableList<HelperClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender_list)

        database = FirebaseDatabase.getInstance().reference.child("users")
        senderList = mutableListOf()

        val recyclerView = findViewById<RecyclerView>(R.id.sender_list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserListAdapter(senderList)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                senderList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(HelperClass::class.java)
                    if (user != null  && user.userType == "Sender") {
                        senderList.add(user)
                    }
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error
            }
        })

        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            // Loop through each user in the list
            for (user in senderList) {
                // Get the radio group for user type
                val userTypeRadioGroup = recyclerView.findViewHolderForAdapterPosition(senderList.indexOf(user))?.itemView?.findViewById<RadioGroup>(R.id.user_type)
                // Get the checked radio button
                val checkedButtonId = userTypeRadioGroup?.checkedRadioButtonId ?: -1
                val checkedRadioButton = userTypeRadioGroup?.findViewById<RadioButton>(checkedButtonId)

                // Update the user type in Firebase database
                if (checkedRadioButton != null) {
                    val userType = when (checkedRadioButton.text) {
                        "Admin" -> "Admin"
                        "User" -> "User"
                        "Sender" -> "Sender"
                        else -> "User"
                    }
                    database.child(user.uid).child("userType").setValue(userType)
                }
            }


            val intent = Intent(this, Admin::class.java)
            startActivity(intent)

            // Show success message
            Toast.makeText(this, "Your changes have been saved successfully", Toast.LENGTH_SHORT).show()
        }
    }
}

