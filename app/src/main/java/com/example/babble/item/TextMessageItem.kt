package com.example.babble.item

import android.content.Context
import com.example.babble.R
import com.example.babble.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class TextMessageItem(val message: TextMessage,
                      val context: Context)
    : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout() = R.layout.item_text_message
}