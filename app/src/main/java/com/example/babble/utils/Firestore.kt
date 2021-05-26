package com.example.babble.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.babble.ChatsActivity
import com.example.babble.SignUpActivity
import com.example.babble.item.PersonItem
import com.example.babble.item.TextMessageItem
import com.example.babble.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.kotlinandroidextensions.Item
import java.lang.NullPointerException

object Firestore {

    //Initiate our Firestore
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val  chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

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
        get() = firestoreInstance.document("user/${FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("UID is null")}")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { DocumentSnapshot ->
            if (!DocumentSnapshot.exists()) {
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?:
                "", "", "", null)
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(email: String = "", fullName: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (fullName.isNotBlank()) userFieldMap["fullName"] = fullName
        if (profilePicturePath != null) {
            userFieldMap["profileImagePath"] = profilePicturePath
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

    fun getOnCreateChatChannel(otherUserId: String,
                                onComplete: (channelId: String) -> Unit) {
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                val newChannel = chatChannelsCollectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef
                    .collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("user").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }

    fun addChatMessageListener(channelId: String, context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                if (e != null) {
                    Log.e("FIRESTORE", "ChatMessage listener error.", e)
                    return@addSnapshotListener
                }
                val items = mutableListOf<Item>()
                snapshot?.documents?.forEach {
                    if (it["type"] == MessageType.TEXT) {
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    } else {
                    }
                    onListen(items)
                }
            }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }
}