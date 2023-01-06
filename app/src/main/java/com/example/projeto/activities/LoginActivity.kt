package com.example.projeto.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.projeto.R
import com.example.projeto.firestore.FirestoreClass
import com.example.projeto.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class LoginActivity :AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var PB : ProgressBar
    private lateinit var showPass : ImageView
    private lateinit var password : EditText
    private lateinit var email : EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth
        PB = findViewById(R.id.mainCategoryPB)
        showPass = findViewById(R.id.imgShowPass)
        password = findViewById(R.id.editText_Password)
        email = findViewById(R.id.editText_Email)

        //Botao Registar Redireciona para a pagina de registo
        val registerText: TextView = findViewById(R.id.textView_registerNow)
        //Botao  Redireciona para a pagina de informacao
        val infoText: TextView = findViewById(R.id.textView_informacao)
        val recuperarPass: TextView = findViewById(R.id.textView_recuperar_pass)

        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        infoText.setOnClickListener {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }

        //Clicar em recupear pass
        recuperarPass.setOnClickListener {

            val intent2 = Intent(this, ForgotPasswordActivity::class.java)
            //metedo a abrir
            startActivity(intent2)
        }

        val loginButton = findViewById<Button>(R.id.button_login)
        loginButton.setOnClickListener {
            performLogin()
        }

        showPass.setOnClickListener{
            view_hide_pass()
        }

    }

    private fun view_hide_pass() {
        if (password.transformationMethod == PasswordTransformationMethod.getInstance()){
            password.transformationMethod = null
        }else {
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            password.text = password.text
        }
    }

    //nova funcao
    fun userLoggedInSucess(user: User) {

        //imprimir os detalhes do usuário no log a partir de agora
        Log.i("Primeiro Nome", user.firstName)
        Log.i("Ultimo Nome", user.lastName)
        Log.i("Email", user.email)
    }


    /**Mostra a loadingBar*/
    fun showLoading(){
        PB.visibility = View.VISIBLE
    }
    /**Esconde a loadingBar*/
    fun  hideLoading(){
        PB.visibility = View.GONE
    }

    //Efetuar Login
    private fun performLogin() {
        //Valores Introduzidos nos campos Email e Password

        //se os campos estiverem vazios, devolve as respetivas mensagens
        if (email.text.isEmpty()) {
            Toast.makeText(this, "Por favor introduza o campo de E-mail",
                Toast.LENGTH_SHORT).show()
            return
        }
        if (password.text.isEmpty()) {
            Toast.makeText(this, "Por favor introduza o campo de Palavra-Passe",
                Toast.LENGTH_SHORT).show()
            return
        }

        //Circulo de progresso
        showLoading()

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

                    hideLoading();
                    Toast.makeText(baseContext, "Login efetuado com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Dados de login incorretos",
                        Toast.LENGTH_SHORT).show()
                    hideLoading();
                }
            }
    }
}

