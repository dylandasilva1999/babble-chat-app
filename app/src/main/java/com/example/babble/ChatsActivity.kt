package com.example.babble

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.babble.fragments.ChatsFragment
import com.example.babble.fragments.ProfileFragment
import com.example.babble.fragments.SearchFragment
import com.example.babble.glide.GlideApp
import com.example.babble.service.MyFirebaseMessagingService
import com.example.babble.service.MyFirebaseMessagingService.Companion.sharedPref
import com.example.babble.utils.Constants
import com.example.babble.utils.Firestore
import com.example.babble.utils.StorageUtil
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.nav_header.*

class ChatsActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        //Reference the Chats and Search Fragments
        val chatsFragment = ChatsFragment()
        val searchFragment = SearchFragment()

        //Set the default fragment view to ChatsFragment
        supportFragmentManager.beginTransaction().apply {
            tv_chats.setBackgroundResource(R.drawable.rounded_corners)
            replaceFragment(ChatsFragment())
            commit()
        }

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Get drawer by ID
        drawer = findViewById(R.id.drawer_layout)
        var navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Menu Icon
        var menu_icon = findViewById<ImageView>(R.id.burger_menu)

        //Toggle for navigation drawer open and close
        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //On Menu Icon Click Open Menu
        menu_icon.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        var profileIcon = findViewById<ImageView>(R.id.profile_image)

        profileIcon.setOnClickListener {
            val intent = Intent (this, ProfileActivity::class.java)
            startActivity(intent)
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_fragment, fragment)
            .commit()
    }

    val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.profile_menu -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                drawer.closeDrawer(GravityCompat.START)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile_layout -> {
                val intent = Intent(this, ChatsActivity::class.java)
                startActivity(intent)
                drawer.closeDrawer(GravityCompat.START)
                return@OnNavigationItemSelectedListener true
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        false
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}