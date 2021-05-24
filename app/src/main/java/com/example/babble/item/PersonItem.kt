package com.example.babble.item

import android.content.Context
import com.bumptech.glide.Glide
import com.example.babble.R
import com.example.babble.glide.GlideApp
import com.example.babble.model.User
import com.example.babble.utils.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_person.*
import kotlinx.android.synthetic.main.item_person.view.*
import kotlinx.android.synthetic.main.nav_header.*

class PersonItem(
    val person: User,
    val userId: String,
    private val context: Context)
    : Item(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {
            textView_name.text = person.fullName
            textView_email.text = person.email
            if (person.profileImagePath != null)
                GlideApp.with(context)
                    .load(StorageUtil.pathToReference(person.profileImagePath))
                    .placeholder(R.drawable.user_profile)
                    .into(viewHolder.profile_image_user)
        }
    }

    override fun getLayout() = R.layout.item_person
}