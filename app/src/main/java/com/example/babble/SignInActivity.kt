package com.example.babble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.babble.utils.Constants
import com.example.noted.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.wy_back_btn

class SignInActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Back Button On Click to MainActivity
        wp_back_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        sign_in_btn.setOnClickListener {
            signInUser()
            et_email_si.setText("")
            et_password_si.setText("")
        }

        mAuth = FirebaseAuth.getInstance()
    }

    private fun signInUser() {
        val email: String = et_email_si.text.toString().trim{ it <= ' '}
        val password: String = et_password_si.text.toString().trim{ it <= ' '}

        if (email == "") {
            showErrorSnackBar("Please enter an email address", true)
        } else if (password == "") {
            showErrorSnackBar("Please enter a password", true)
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        sign_in_btn.visibility = View.INVISIBLE
                        sign_in_animation.visibility = View.VISIBLE
                        sign_in_animation.playAnimation()

                        //Logging in user and navigate to ChatsActivity
                        loginUserSuccess(firebaseUser.uid)
                    }
                    else {
                        sign_in_btn.visibility = View.VISIBLE
                        showErrorSnackBar("Error Message: " + task.exception?.message.toString(), true)
                    }
                }
        }
    }

    //Successfully logged in user function
    fun loginUserSuccess(uid: String) {

        //Navigation after Sign Up
        val runnable = Runnable {
            val intent = Intent(this, ChatsActivity::class.java)
            intent.putExtra(Constants.LOGGED_IN_ID, uid)
            startActivityForResult(intent, RC_SIGN_IN)
            finish()
        }

        Handler().postDelayed(runnable, 2500)
    }
}