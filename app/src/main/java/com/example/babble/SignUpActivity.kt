package com.example.babble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import com.example.babble.model.User
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.example.noted.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        var tc_text = findViewById<TextView>(R.id.tc_text)

        tc_text.setOnClickListener {
            val intent = Intent(this, TermsConditionsActivity::class.java)
            startActivity(intent)
        }

        //Back Button On Click to MainActivity
        wy_back_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Sign Up button click reset edit fields
        sign_up_btn.setOnClickListener {
            registerUser()
            et_fullname_su.setText("")
            et_email_su.setText("")
            et_password_su.setText("")
        }

        //Get instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }

    //Register User Function
    private fun registerUser() {
        val fullName: String = et_fullname_su.text.toString().trim{ it <= ' '}
        val email: String = et_email_su.text.toString().trim{ it <= ' '}
        val password: String = et_password_su.text.toString().trim{ it <= ' '}

        if (fullName == "") {
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

                        //Creating the user data
                        val user = User(
                            firebaseUser.uid,
                            fullName,
                            email
                        )

                        sign_up_btn.visibility = View.INVISIBLE
                        sign_up_animation.visibility = View.VISIBLE
                        sign_up_animation.playAnimation()

                        //Adding user data to Firestore
                        Firestore().registerUser(this, user)
                    }
                    else {
                        sign_up_btn.visibility = View.VISIBLE
                        showErrorSnackBar("Error Message: " + task.exception?.message.toString(), true)
                    }
                }
        }
    }

    //Successfully registered user function
    fun registerUserSuccess(uid: String) {

        //Navigation after Sign Up
        val runnable = Runnable {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(Constants.LOGGED_IN_ID, uid)
            startActivity(intent)
            finish()
        }

        Handler().postDelayed(runnable, 1500)
    }

}