package com.example.babble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.babble.fragments.ChatsFragment
import com.example.babble.fragments.SearchFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.nav_header.*

class ChatsActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        //Make the View FullScreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Get drawer by ID
        drawer = findViewById(R.id.drawer_layout)
        var navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Menu Icon
        var menu_icon = findViewById<ImageView>(R.id.burger_menu)

        //Toggle for navigation drawer open and close
        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //On Menu Icon Click Open Menu
        menu_icon.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

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

    val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.profile_menu -> {
                //Add Profile Intent Activity Here
                drawer.closeDrawer(GravityCompat.START)
                return@OnNavigationItemSelectedListener true
            }
            R.id.chats_menu-> {
                val intent = Intent(this, ChatsActivity::class.java)
                startActivity(intent)
                drawer.closeDrawer(GravityCompat.START)
                return@OnNavigationItemSelectedListener true
            }
            R.id.users_menu -> {
                //Add Users Intent Activity Here
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