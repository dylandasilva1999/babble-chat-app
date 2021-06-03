package com.example.babble.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.babble.R
import com.example.babble.SignInActivity
import com.example.babble.glide.GlideApp
import com.example.babble.utils.Firestore
import com.example.babble.utils.StorageUtil
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var profileJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.apply {
            iv_instagram.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.instagram.com/")
                startActivity(openURL)
            }

            iv_facebook.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.facebook.com/")
                startActivity(openURL)
            }

            iv_twitter.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.twitter.com/")
                startActivity(openURL)
            }

            profile_image.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_IMAGE)
            }

            save_info_btn.setOnClickListener {
                if (::selectedImageBytes.isInitialized) {
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        Firestore.updateCurrentUser(editText_email.text.toString(),
                            editText_fullname.text.toString(), imagePath)
                    }
                } else {
                    Firestore.updateCurrentUser(editText_email.text.toString(),
                        editText_fullname.text.toString(), null)
                }
            }

            sign_out_btn.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(this@ProfileFragment.context!!)
                    .addOnCompleteListener {
                        startActivity(intentFor<SignInActivity>().newTask().clearTask())
                    }
            }
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            var selectedImagePath = data.data
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
        Firestore.getCurrentUser { user ->
            if (this@ProfileFragment.isVisible) {
                editText_fullname.setText(user.fullName)
                editText_email.setText(user.email)
                if (!profileJustChanged && user.profileImagePath != null) {
                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profileImagePath))
                        .placeholder(R.drawable.default_profile_screen)
                        .into(profile_image)
                }
            }
        }
    }
}