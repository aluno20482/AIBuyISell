package com.example.projeto.activities

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
<<<<<<< HEAD

=======
import androidx.compose.ui.graphics.Color
>>>>>>> 6e3a79e5511e8fafdc12fbc4742cf258d964ccb2
import com.example.projeto.R
import com.example.projeto.firestore.FirestoreClass
import com.example.projeto.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp



class LoginActivity : MainActivity(){

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


<<<<<<< HEAD
        val homeButton = findViewById<Button>(R.id.button_muda)

        val addProduct = findViewById<Button>(R.id.button_addProduct)

        addProduct.setOnClickListener {
            verAddProduct()
        }

        homeButton.setOnClickListener {
           verHome()
=======
        /*
        val button: TextView = findViewById(R.id.textView_recuperar_pass)
        button.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
>>>>>>> 6e3a79e5511e8fafdc12fbc4742cf258d964ccb2
        }

         */


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

        val homeButton = findViewById<Button>(R.id.button_muda)
        homeButton.setOnClickListener {
            verHome()
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


<<<<<<< HEAD
    private fun verAddProduct(){
        val intent = Intent(this, AddItemActivity::class.java)
        startActivity(intent)
    }

    private fun verHome(){
=======
    private fun verHome() {
>>>>>>> 6e3a79e5511e8fafdc12fbc4742cf258d964ccb2
        // Sign in success, agora vamos para a proxima activity
        val intent = Intent(this, ShoppingActivity::class.java)
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
                Toast.LENGTH_SHORT
            ).show()
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
                    val intent = Intent(this, MainActivity::class.java)
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


            .addOnFailureListener {
                Toast.makeText(baseContext, "Falha na autenticação.${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }

    }
}