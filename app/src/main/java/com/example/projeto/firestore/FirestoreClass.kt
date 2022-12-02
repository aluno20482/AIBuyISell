package com.example.projeto.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.projeto.LoginActivity
import com.example.projeto.RegisterActivity
import com.example.projeto.models.User
import com.example.projeto.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()
    //val mFireStore = Firebase.firestore

    //Regista o user na base de dados
    fun registerUser(activity: RegisterActivity, userInfo: User) {
        //mFireStore.collection("users")

        //O "users" é o nome da coleção. Se a coleção já estiver criada então não será criada então não vai criar a mesma  de novo
        mFireStore.collection(Constants.USERS)
             //ID do documento para campos de usuários. Aqui o documento é o User ID
            .document(userInfo.id)
             //Aqui, as userInfo são Field e SetOption está definido para merge. É para se quisermos fundir mais tarde em vez de substituir os campos
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                //Aqui chame uma função de atividade base para transferir o resultado para ela
                activity.userResgistrationSucess()
                activity.hideProgressDialog()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Erro ao registar o utlizador",
                    e
                )

            }
    }


    fun getCurrentUserID():String{

        //Uma instância do usuário atual usando Firebase Auth
        val currentUser  = FirebaseAuth.getInstance().currentUser

        //Uma variável para atribuir o currentUserId se não for nulo ou então ficará em branco
        var currentUserID = ""
        if(currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }


    // Mostar detalhes do user
    fun getUsersDetails(activity: Activity){

        // Aqui passamos o nome da coleção da qual queremos os dados
        mFireStore.collection(Constants.USERS)

            // O id do documento para obter os campos de usuário
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Aqui recebemos o instantâneo do documento que é convertido no objeto do modelo de dados do usuário
                val user = document.toObject(User::class.java)!!

                //Context.getSharedPreferences("mySharedPrefData",MODE_PRIVATE)

                val sharedPreferences =

                    activity.getSharedPreferences(
                        Constants.MINHALOJA,
                        //ver qual o tipo de import para context
                        Context.MODE_PRIVATE
                    )


                //context.getSharedPreferences("CASPreferences", Context.MODE_PRIVATE);

                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                //key :         LOGGED_IN_USERNAME  Telmo Silva
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                //INICIO
                when(activity){
                    is LoginActivity -> {

                        //Chamar uma função de atividade base para transferir o resultado para ela
                        activity.userLoggedInSucess(user)
                    }
                }

                //FIM
            }
            .addOnFailureListener {  e ->

            }
    }

}