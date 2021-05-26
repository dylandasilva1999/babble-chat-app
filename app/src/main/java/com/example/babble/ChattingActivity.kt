package com.example.babble

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import org.jetbrains.anko.toast

class ChattingActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val otherUserId = intent.getStringExtra(Constants.USER_ID)
        if (otherUserId != null) {
            Firestore.getOnCreateChatChannel(otherUserId) { channelId ->
                messageListenerRegistration = Firestore.addChatMessageListener(channelId, this, this::onMessageChanged)
            }
        }
    }

    private fun onMessageChanged(message: List<Item>) {
        toast("onMessageChangedRunning!")
    }
}