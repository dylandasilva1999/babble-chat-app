package com.example.babble

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babble.model.*
import com.example.babble.service.MyFirebaseMessagingService
import com.example.babble.service.RetrofitInstance
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.example.babble.utils.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.android.synthetic.main.activity_chatting.wp_back_btn
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2

class ChattingActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    var firebaseUser: FirebaseUser? = null

    private lateinit var currentChannelId: String
    private lateinit var currentUser: User
    private lateinit var otherUserId: String

    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        chatting_user_name.text = intent.getStringExtra(Constants.USER_NAME)

        val userName = intent.getStringExtra(Constants.USER_NAME)

        MyFirebaseMessagingService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            MyFirebaseMessagingService.token = it.token
        }

        Firestore.getCurrentUser {
            currentUser = it
        }

        wp_back_btn.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }

        otherUserId = intent.getStringExtra(Constants.USER_ID).toString()

            Firestore.getOnCreateChatChannel(otherUserId!!) { channelId ->

                currentChannelId = channelId
                messageListenerRegistration = Firestore.addChatMessageListener(channelId, this, this::updateRecyclerView)

                imageView_send.setOnClickListener {
                    val messageToSend = TextMessage(editText_message.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, otherUserId, currentUser.fullName)
                    editText_message.setText("")
                    Firestore.sendMessage(messageToSend, channelId)
                    topic = "topics/$otherUserId"
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/$otherUserId")
                    PushNotification(NotificationData(userName!!, messageToSend),
                        topic).also {
                        sendNotification(it)
                    }
                }

                fab_send_image.setOnClickListener {
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/$otherUserId")
                    val intent = Intent().apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                    }
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
                }
            }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                    ImageMessage(imagePath,
                        Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        otherUserId,
                        currentUser.fullName)
                Firestore.sendMessage(messageToSend, currentChannelId)
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChattingActivity)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    messagesSection = Section(messages)
                    add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
    }
}