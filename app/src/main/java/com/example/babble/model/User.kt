package com.example.babble.model

class User (
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val profileImagePath: String = ""
)
{
    constructor(): this("", "", "", "")
}

