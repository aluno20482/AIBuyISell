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


        //Botao Registar Redireciona para a pagina de registo
        val registerText: TextView = findViewById(R.id.textView_registerNow)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //Botao  Redireciona para a pagina de informacao
        val infoText: TextView = findViewById(R.id.textView_informacao)
        infoText.setOnClickListener {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }


        //Clicar em recupear pass
        val recuperarPass: TextView = findViewById(R.id.textView_recuperar_pass)
        recuperarPass.setOnClickListener {

            val intent2 = Intent(this, ForgotPasswordActivity::class.java)
            //metedo a abrir
            startActivity(intent2)
        }

        val loginButton = findViewById<Button>(R.id.button_login)
        loginButton.setOnClickListener {
            performLogin()
        }

        val loginMenu = findViewById<Button>(R.id.button_mudaPSlider)
        loginMenu.setOnClickListener {
            val intent3 = Intent(this, MenuActivity::class.java)
            //metedo a abrir
            startActivity(intent3)
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


    }


    private fun verAddProduct() {
        val intent = Intent(this, AddItemActivity::class.java)
        startActivity(intent)
    }




    private fun verFavs() {
        // Sign in success, agora vamos para a proxima activity
        //val intent = Intent(this, FavoriteCategoryFragment::class.java)
        //startActivity(intent)
    }


    private fun verEmailAEnviar() {
        val intent = Intent(this, SubmitEmailActivity::class.java)
        startActivity(intent)

    }


    //Efetuar Login
    private fun performLogin() {
        //Circulo de progresso
        showProgressDialog()
        //Valores Introduzidos nos campos Email e Password
        var email = findViewById<EditText>(R.id.editText_Email)
        var password = findViewById<EditText>(R.id.editText_Password)


        //se os 2 campos email e password estiverem vazios, devolve a mensagem ao ultizador
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Por favor preencha todos os campos",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }


        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()


        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->

                //Tarefa com sucesso
                if (task.isSuccessful) {

                    //Mostra os detalhes do user
                    FirestoreClass().getUsersDetails(this@LoginActivity)

                    // Sign in success, agora vamos para a proxima activity
                    //val intent = Intent(this, ShoppingActivity::class.java)
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)

                    hideProgressDialog();
                    Toast.makeText(baseContext, "Login efetuado com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Falha na autenticação.",
                        Toast.LENGTH_SHORT).show()
                    hideProgressDialog();
                }
            }

            .addOnFailureListener {
                Toast.makeText(baseContext, "Falha na autenticação.${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }

    }
}


/*

 //tartamento de execões ao criar conta
 .addOnFailureListener{exececao ->
     val mensagemErro = when(exececao){
         is FirebaseAuthWeakPasswordException -> "Introduza uma palavra-passe com o mínimo 6 digitos"
         is FirebaseAuthInvalidCredentialsException -> "Digite um email válido"
         is FirebaseAuthUserCollisionException -> "Conta Registada"
         is FirebaseNetworkException -> "Sem ligação a Internet"
         else -> "Erro ao registar utilizador"
     }
     Toast.makeText(this,mensagemErro,
         Toast.LENGTH_SHORT
     ).show()
      */
