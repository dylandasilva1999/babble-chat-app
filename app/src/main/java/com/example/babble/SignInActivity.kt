package com.example.babble

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.example.noted.BaseActivity
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SignInActivity : BaseActivity() {

    //Notification is based on Channel
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"

    //Notification ID
    val NOTIFICATION_ID = 0

    private lateinit var mAuth: FirebaseAuth
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //Allow to create notifications
        createNotificationChannel()

        //Create notifications
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Babble")
            .setContentText("Successfully Signed In!")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

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
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                Firestore.initCurrentUserIfFirstTime {
                    startActivity(intentFor<ChatsActivity>().newTask().clearTask())

                    var registrationToken = FirebaseInstanceId.getInstance().token
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return
                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        showErrorSnackBar("No Network", true)
                    ErrorCodes.UNKNOWN_ERROR ->
                        showErrorSnackBar("Unknown Error", true)
                }
            }
        }
    }

    fun createNotificationChannel() {
        //Check the SDK version is greater than oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create a notification channel and send the ID, Name & importance
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                //Set the notification LED colour
                enableLights(true)
                lightColor = Color.YELLOW
            }

            //Set manager as Notification Service
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
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
    private fun loginUserSuccess(uid: String) {

        //Navigation after Sign In
        val runnable = Runnable {
            val intent = Intent(this, ChatsActivity::class.java)
            intent.putExtra(Constants.LOGGED_IN_ID, uid)
            startActivityForResult(intent, RC_SIGN_IN)
            finish()
        }

        Handler().postDelayed(runnable, 2500)
    }
}