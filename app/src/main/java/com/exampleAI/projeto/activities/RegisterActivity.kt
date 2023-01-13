package com.exampleAI.projeto.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.exampleAI.projeto.R
import com.exampleAI.projeto.firestore.FirestoreClass
import com.exampleAI.projeto.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var showPass : ImageView
    private lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializa Firebase Auth
        auth = Firebase.auth
        showPass = findViewById(R.id.imgShowPass)
        password = findViewById(R.id.editText_Password)

        val loginText: TextView = findViewById(R.id.textView_loginNow)

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.button_Create)
        registerButton.setOnClickListener {
            performSignUp()
        }

        showPass.setOnClickListener{
            view_hide_pass()
        }
    }

    /**Função para realizar inscrição de novo utlizador*/
    private fun performSignUp() {
        //showProgressDialog()

        var email = findViewById<EditText>(R.id.editText_Email)
        var password = findViewById<EditText>(R.id.editText_Password)
        var primeiroNome = findViewById<EditText>(R.id.editText_primeiroNome)
        var ultimoNome = findViewById<EditText>(R.id.editText_ultimoNome)

        if (email.text.isEmpty() || password.text.isEmpty() || primeiroNome.text.isEmpty() || ultimoNome.text.isEmpty()) {
            Toast.makeText(
                this, "Por favor preencha todos os campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val inputEmail = email.text.toString().trim { it <= ' ' }
        val inputPassword = password.text.toString().trim { it <= ' ' }

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->

                //showProgressDialog();

                //If the registartion is sucessfuly done
                if (task.isSuccessful) {

                    //aqui vai se guardar os dados do utilizador e colocar uma progress bar
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val userInfo = User(
                        //nota deviam ser aqui os nomes igual a base de dados
                        firebaseUser.uid,
                        primeiroNome.text.toString().trim { it <= ' ' },
                        ultimoNome.text.toString().trim { it <= ' ' },
                        email.text.toString().trim { it <= ' ' }
                    )

                    FirestoreClass().registerUser(this@RegisterActivity, userInfo)


                    val mFireStore = Firebase.firestore

                    mFireStore.collection("users")
                        .document(userInfo.id)
                        .set(userInfo, SetOptions.merge())
                        //.add(userInfo)
                        .addOnSuccessListener {
                            userResgistrationSucess()
                            //hideProgressDialog()

                            //limpar dados
                            primeiroNome.text.clear()
                            ultimoNome.text.clear()
                            email.text.clear()
                            password.text.clear()
                        }
                        .addOnFailureListener { e ->

                            //hideProgressDialog()
                            Log.e(
                                javaClass.simpleName,
                                "Error while registering the user.",
                                e
                            )
                            Toast.makeText(
                                baseContext, "deu erro",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {
                //hideProgressDialog()
                Toast.makeText(
                    this, "Error occured ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    /**Devolve a mensagem quando o user se regista*/
    fun userResgistrationSucess() {
        //Hide the progress bar
        //hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_sucess),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun view_hide_pass() {
        if (password.transformationMethod == PasswordTransformationMethod.getInstance()){
            password.transformationMethod = null
        }else {
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            password.text = password.text
        }
    }

}