package com.example.babble

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.util.Log
import android.view.Gravity.apply
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ContentView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import com.example.babble.glide.GlideApp
import com.example.babble.utils.Firestore
import com.example.babble.utils.StorageUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.alertDialogLayout
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    private val isVisible = 1
    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var profileJustChanged = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        wp_back_btn.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }

            profile_image.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

            save_info_btn.setOnClickListener {
                if (::selectedImageBytes.isInitialized) {
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) {imagePath ->
                        Firestore.updateCurrentUser(editText_email.text.toString(),
                            editText_fullname.text.toString(), imagePath)
                    }
                } else {
                    Firestore.updateCurrentUser(editText_email.text.toString(),
                        editText_fullname.text.toString(),  "")
                }
            }

            sign_out_btn.setOnClickListener {
                FirebaseAuth.getInstance()
                    .signOut()
                startActivity(intentFor<SignInActivity>().newTask().clearTask())
            }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            var selectedImagePath = data.data
            val activity = ProfileActivity()
            var selectedImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            GlideApp.with(this)
                .load(selectedImageBytes)
                .into(profile_image)

            profileJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()
        var activity = ProfileActivity()
        Firestore.getCurrentUser { user ->
            if (view.toggleVisibility()) {
                editText_fullname.setText(user.fullName)
                editText_email.setText(user.email)
                if (!profileJustChanged && user.profileImagePath != "") {
                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profileImagePath))
                        .placeholder(R.drawable.default_profile_screen)
                        .into(profile_image)
                }
            }
        }
    }

    fun View.toggleVisibility() {
        if (visibility == View.VISIBLE) {
            visibility = View.INVISIBLE
        } else {
            visibility = View.VISIBLE
        }
    }
}