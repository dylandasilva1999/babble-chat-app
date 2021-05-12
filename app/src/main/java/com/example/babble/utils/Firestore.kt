package com.example.babble.utils

import com.example.babble.ChatsActivity
import com.example.babble.SignUpActivity
import com.example.babble.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Firestore {

    //Initiate our Firestore
    private val db = FirebaseFirestore.getInstance()

    //Register user to Firestore database
    fun registerUser(activity: SignUpActivity, userInfo: User) {

        //Collection -> document (uid from auth)
        db.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge()) //set the new user (merge allows us to update user)
            .addOnSuccessListener {
                //Do this on success, navigate to next activity
                activity.registerUserSuccess(userInfo.id)
            }
    }

}