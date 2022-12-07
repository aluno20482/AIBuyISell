package com.example.projeto.firestore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.TextView
import com.example.projeto.R
import com.example.projeto.activities.LoginActivity
import com.example.projeto.activities.RegisterActivity

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


    fun getCurrentUserID(): String {

        //Uma instância do usuário atual usando Firebase Auth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //Uma variável para atribuir o currentUserId se não for nulo ou então ficará em branco
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }


    // Alterar dados na BD
    fun alterarDados(activity: Activity, firstName: String, lastName: String, mobile: String, address: String) {

        mFireStore.collection(Constants.USERS).document(getCurrentUserID())
            .update("firstName", firstName, "lastName", lastName, "mobile", mobile, "address", address)

    }


    // Mostar detalhes do user
    fun getUsersDetails(activity: Activity) {

        // Aqui passamos o nome da coleção da qual queremos os dados
        mFireStore.collection(Constants.USERS)

            // O id do documento para obter os campos de usuário
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Aqui recebemos o instantâneo do documento que é convertido no objeto do modelo de dados do usuário
                val user = document.toObject(User::class.java)!!


                //teste update base de dados
                // mFireStore.collection(Constants.USERS).document(getCurrentUserID()).update("lastName", "John")


                //Context.getSharedPreferences("mySharedPrefData",MODE_PRIVATE)

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MINHALOJA,
                        //ver qual o tipo de import para context
                        Context.MODE_PRIVATE
                    )


                //context.getSharedPreferences("CASPreferences", Context.MODE_PRIVATE);

                val editorNome: SharedPreferences.Editor = sharedPreferences.edit()
                //key :         LOGGED_IN_USERNAME  Telmo Silva
                editorNome.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editorNome.apply()

                val editortelefone: SharedPreferences.Editor = sharedPreferences.edit()
                editortelefone.putString(
                    Constants.LOGGED_IN_CONTACTO,
                    "${user.mobile}"
                )
                editortelefone.apply()

                val editorMorada: SharedPreferences.Editor = sharedPreferences.edit()
                //key :         LOGGED_IN_USERNAME  Telmo Silva
                editorMorada.putString(
                    Constants.LOGGED_IN_MORADA,
                    " ${user.address}"
                )
                editorMorada.apply()



                //INICIO
                when (activity) {
                    is LoginActivity -> {

                        //Chamar uma função de atividade base para transferir o resultado para ela
                        activity.userLoggedInSucess(user)
                    }
                }

                //FIM
            }
            .addOnFailureListener { e ->

            }
    }

}