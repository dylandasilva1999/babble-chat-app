package com.example.babble.model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}