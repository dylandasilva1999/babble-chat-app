package com.example.babble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babble.model.MessageType
import com.example.babble.model.TextMessage
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.android.synthetic.main.activity_chatting.wp_back_btn
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.toast
import java.util.*

class ChattingActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        chatting_user_name.text = intent.getStringExtra(Constants.USER_NAME)

        wp_back_btn.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }

        val otherUserId = intent.getStringExtra(Constants.USER_ID)
        if (otherUserId != null) {
            Firestore.getOnCreateChatChannel(otherUserId) { channelId ->
                messageListenerRegistration = Firestore.addChatMessageListener(channelId, this, this::updateRecyclerView)
                imageView_send.setOnClickListener {
                    val messageToSend = TextMessage(editText_message.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                    editText_message.setText("")
                    Firestore.sendMessage(messageToSend, channelId)
                }

                fab_send_image.setOnClickListener {
                    //TODO: Send Image Messages
                }
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