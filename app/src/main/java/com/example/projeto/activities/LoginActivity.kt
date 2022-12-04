package com.example.projeto.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projeto.R
import com.example.projeto.firestore.FirestoreClass
import com.example.projeto.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : MainActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val registerText:TextView = findViewById(R.id.textView_registerNow)

        registerText.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.button_login)

        val homeButton = findViewById<Button>(R.id.button_muda)

        homeButton.setOnClickListener {
           verHome()
        }

        loginButton.setOnClickListener {
            performLogin()
        }
    }

    //nova funcao
    fun userLoggedInSucess(user: User) {
        //ocultar a caixa de diálogo de progresso
        //hideProgressDialog()

        //imprimir os detalhes do usuário no log a partir de agora
        Log.i("Primeiro Nome", user.firstName)
        Log.i("Ultimo Nome", user.lastName)
        Log.i("Email", user.email)


        /*
        //IDEIA
        //Se o utlizador não tiver os dados completos ao iniciar a aplicação
        //Quando iniciar a mesma vai para a pagina para completar os dados
        if(user.profileCompleted == 0){

            //Se o perfil do usuário estiver incompleto, inicie a atividade do perfil do usuário
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            //intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            //Se o perfil do usuário estiver Completo, redirecione o usuário para a tela principal após o login
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

*/

        //redirecionar o usuário para a tela principal após o login
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }


    private fun verHome(){
        // Sign in success, agora vamos para a proxima activity
        val intent = Intent(this, ShoppingActivity::class.java)
        startActivity(intent)

    }

    private fun performLogin(){
        showProgressDialog()

        var email = findViewById<EditText>(R.id.editText_Email)
        var password = findViewById<EditText>(R.id.editText_Password)

        if(email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Please fill all the fields",
                Toast.LENGTH_SHORT
            ).show()
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    //Mostra os detalhes do user
                    FirestoreClass().getUsersDetails(this@LoginActivity)


                    // Sign in success, agora vamos para a proxima activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    hideProgressDialog();
                    Toast.makeText(baseContext, "Login Sucess.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    hideProgressDialog();
                }
            }
            .addOnFailureListener{
                Toast.makeText(baseContext,"Authentication failed.${it.localizedMessage}",
                Toast.LENGTH_SHORT).show()
            }

    }
}