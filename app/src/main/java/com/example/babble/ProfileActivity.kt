package com.example.babble

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.babble.fragments.ProfileFragment
import com.example.babble.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.*

class ProfileActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        val profileFragment = ProfileFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_profile, profileFragment)
            commit()
        }

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        wp_back_btn.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }
    }
}