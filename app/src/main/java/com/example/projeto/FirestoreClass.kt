package com.example.projeto


import android.util.Log
import android.widget.Toast
import com.example.projeto.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()
    //val mFireStore = Firebase.firestore

    fun registerUser(activity: RegisterActivity,userInfo: User) {



//teste  t
        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userResgistrationSucess()
                activity.hideProgressDialog()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                     "Error while registering the user.",
                    e
                )

            }
    }

}