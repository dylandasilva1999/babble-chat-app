package com.example.babble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babble.fragments.ChatsFragment
import com.example.babble.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_chats.*

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Reference the Chats and Search Fragments
        val chatsFragment = ChatsFragment()
        val searchFragment = SearchFragment()

        //Set the default fragment view to ChatsFragment
        supportFragmentManager.beginTransaction().apply {
            tv_chats.setBackgroundResource(R.drawable.rounded_corners)
            replace(R.id.fl_fragment, chatsFragment)
            commit()
        }

        tv_chats.setOnClickListener {
            tv_chats.setBackgroundResource(R.drawable.rounded_corners)
            tv_search.setBackgroundResource(0)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment, chatsFragment)
                addToBackStack(null)
                commit()
            }
        }

        tv_search.setOnClickListener {
            tv_search.setBackgroundResource(R.drawable.rounded_corners)
            tv_chats.setBackgroundResource(0)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment, searchFragment)
                addToBackStack(null)
                commit()
            }
        }

    }
}