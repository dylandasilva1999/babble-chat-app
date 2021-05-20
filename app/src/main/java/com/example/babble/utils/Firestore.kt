package com.example.babble.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.babble.ChatsActivity
import com.example.babble.SignUpActivity
import com.example.babble.item.PersonItem
import com.example.babble.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.xwray.groupie.kotlinandroidextensions.Item
import java.lang.NullPointerException

object Firestore {

    //Initiate our Firestore
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

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
            .addOnFailureListener {
                activity.showErrorSnackBar("Error while registering user...", true)
            }
    }

    private val currentUserDocRef: DocumentReference
    get() = db.document("user/${FirebaseAuth.getInstance().currentUser?.uid
        ?: throw NullPointerException("UID is null and does not exist")}")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { DocumentSnapshot ->
            if (!DocumentSnapshot.exists()) {
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?:
                "", "", "", "")
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(email: String = "", fullName: String = "", profilePicturePath: String = "") {
        val userFieldMap = mutableMapOf<String, Any>()
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (fullName.isNotBlank()) userFieldMap["fullName"] = fullName
        if (profilePicturePath != "") {
            userFieldMap["profilePicturePath"] = profilePicturePath
        }
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }
    }

    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("user")
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                if (e != null) {
                    Log.e("FIRESTORE", "Users listener error.", e)
                    return@addSnapshotListener
                }
                val items = mutableListOf<Item>()
                snapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                    }
                }
                onListen(items)

            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}