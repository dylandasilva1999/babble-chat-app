package com.example.babble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.noted.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Back Button On Click to MainActivity
        wy_back_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        sign_up_btn.setOnClickListener {
            registerUser()

        }

        mAuth = FirebaseAuth.getInstance()
    }

    private fun registerUser() {
        val username: String = et_username_su.text.toString().trim{ it <= ' '}
        val email: String = et_email_su.text.toString().trim{ it <= ' '}
        val password: String = et_password_su.text.toString().trim{ it <= ' '}

        if (username == "") {
            showErrorSnackBar("Please enter a username", true)
        } else if (email == "") {
            showErrorSnackBar("Please enter an email address", true)
        } else if (password == "") {
            showErrorSnackBar("Please enter a password", true)
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        showErrorSnackBar("Successfully registered user id ${firebaseUser.uid}", false)
                    }
                    else {
                        showErrorSnackBar("Error Message: " + task.exception?.message.toString(), true)
                    }
                }
        }
    }
}