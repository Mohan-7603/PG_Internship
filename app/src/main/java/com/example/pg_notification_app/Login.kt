package com.example.pg_notification_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class Login : AppCompatActivity() {


    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signupRedirectedText: TextView
    private lateinit var loginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginUsername = findViewById(R.id.login_username)
        loginPassword = findViewById(R.id.login_password)
        signupRedirectedText = findViewById(R.id.signupRedirectText)
        loginButton = findViewById(R.id.login_button)


        loginButton.setOnClickListener {
            if (!validateUsername() || !validatePassword()) {
                // do nothing
            } else {
                checkUser()
            }
        }

        signupRedirectedText.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

    }

    private fun validateUsername(): Boolean {
        val user = loginUsername.text.toString().trim()

        return if (user.isEmpty()) {
            loginUsername.error = "Username cannot be empty!"
            false
        } else {
            loginUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = loginPassword.text.toString()
        return if (value.isEmpty()) {
            loginPassword.error = "Password cannot be empty!"
            false
        } else {
            loginPassword.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = loginUsername.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    loginUsername.error = null
                    for (userSnapshot in snapshot.children) {
                        val passwordFromDB =
                            userSnapshot.child("password").getValue(String::class.java)
                        val userType = userSnapshot.child("userType").getValue(String::class.java)
                        if (passwordFromDB != null && passwordFromDB == userPassword) {
//  important do not erase!
                            if (userType == "Admin") {
                                val intent = Intent(this@Login, Admin::class.java)
                                startActivity(intent)
                            } else if(userType == "Sender") {
                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                            }else{
                                val intent = Intent(this@Login, User::class.java)
                                startActivity(intent)
                            }


                            return
                        }
                    }
                    loginPassword.error = "Invalid Credentials!"
                    loginPassword.requestFocus()
                } else {
                    loginUsername.error = "User does not exist!"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle cancellation
            }
        })
    }
}
