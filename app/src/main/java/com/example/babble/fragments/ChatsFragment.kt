package com.example.babble.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babble.ChatsActivity
import com.example.babble.ChattingActivity
import com.example.babble.R
import com.example.babble.glide.GlideApp
import com.example.babble.item.PersonItem
import com.example.babble.model.User
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.example.babble.utils.StorageUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.activity_chats.profile_image
import kotlinx.android.synthetic.main.activity_chats.view.*
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.startActivity

class ChatsFragment : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var chatsSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        userListenerRegistration = Firestore.addUsersListener(this.activity!!, this::updateRecyclerView)

        Firestore.getCurrentUser { user ->
            Log.d("Get User Error", user.toString())
        }

        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Firestore.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recycler_view_chats.apply {
                layoutManager = LinearLayoutManager(this@ChatsFragment.context)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    chatsSection = Section(items)
                    add(chatsSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems()  = chatsSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()
    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonItem) {
            startActivity<ChattingActivity>(
                Constants.USER_NAME to item.person.fullName,
                Constants.USER_ID to item.userId
            )
        }
    }
}