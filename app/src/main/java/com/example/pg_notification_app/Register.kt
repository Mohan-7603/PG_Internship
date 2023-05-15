package com.example.pg_notification_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Properties
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

class Register : AppCompatActivity() {

    private lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectedText: TextView
    private lateinit var signupButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        signupUsername = findViewById(R.id.signup_username)
        signupEmail = findViewById(R.id.signup_email)
        signupPassword = findViewById(R.id.signup_password)
        loginRedirectedText = findViewById(R.id.loginRedirectText)
        signupButton = findViewById(R.id.signup_button)

        firebaseAuth = FirebaseAuth.getInstance()

        signupButton.setOnClickListener {
            if (validateUsername() && validatePassword() && validateEmail()) {

                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val reference: DatabaseReference = database.getReference("users")


                val username = signupUsername.text.toString()
                val email = signupEmail.text.toString()
                val password = signupPassword.text.toString()

                // create a new user in Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val uid = user!!.uid

                            // retrieve the device token
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                                // save the user ID and device token to the database
                                val helperClass = HelperClass(username,email,password, uid, token,"User")
                                reference.child(uid).setValue(helperClass)

                                Toast.makeText(
                                    this,
                                    "Your Account has been successfully created",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Registration failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }

        loginRedirectedText.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }


    private fun validateUsername(): Boolean {
        val user = signupUsername.text.toString().trim()

        return if (user.isEmpty()) {
            signupUsername.error = "Username cannot be empty!"
            false
        } else {
            signupUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = signupPassword.text.toString()
        return if (value.isEmpty()) {
            signupPassword.error = "Password cannot be empty!"
            false
        } else {
            signupPassword.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = signupEmail.text.toString().trim()

        return if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.error = "Please enter a valid email address!"
            false
        } else {
            signupEmail.error = null
            true
        }
    }

//    private fun generateOTP(): String {
//        val length = 6
//        val random = Random(System.currentTimeMillis())
//        val otp = StringBuilder(length)
//        for (i in 0 until length) {
//            otp.append(random.nextInt(10))
//        }
//        return otp.toString()
//    }

//    private fun sendOTP(email: String, otp: String) {
//        val props = Properties()
//        props["mail.smtp.host"] = "smtp.gmail.com"
//        props["mail.smtp.socketFactory.port"] = "465"
//        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
//        props["mail.smtp.auth"] = "true"
//        props["mail.smtp.port"] = "465"
//
//        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
//            override fun getPasswordAuthentication(): PasswordAuthentication {
//                return PasswordAuthentication("7603mohanram@gmail.com", "9445923139")
//            }
//        })
//
//        try {
//            val message = MimeMessage(session)
//            message.setFrom(InternetAddress("7603mohanram@gmail.com"))
//            message.addRecipient(
//                Message.RecipientType.TO,
//                InternetAddress(signupEmail.text.toString())
//            )
//            message.subject = "OTP Verification"
//            message.setText("Your OTP is: $otp")
//            Transport.send(message)
//            Toast.makeText(
//                this,
//                "OTP is sent, kindly check your email!",
//                Toast.LENGTH_SHORT
//            ).show()
//        } catch (e: MessagingException) {
//            e.printStackTrace()
//            Toast.makeText(
//                this,
//                "Your email does not exist!",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
}



//                val generatedOTP = generateOTP()
//                sendOTP(signupEmail.toString(), generatedOTP)
//
//                val otpDigit1 = findViewById<EditText>(R.id.otp_digit1)
//                val otpDigit2 = findViewById<EditText>(R.id.otp_digit2)
//                val otpDigit3 = findViewById<EditText>(R.id.otp_digit3)
//                val otpDigit4 = findViewById<EditText>(R.id.otp_digit4)
//                val enteredOTP =
//                    "${otpDigit1.text}${otpDigit2.text}${otpDigit3.text}${otpDigit4.text}"
//
//
//                if (enteredOTP == generatedOTP) {