package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projeto.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : MainActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val loginText:TextView = findViewById(R.id.textView_loginNow)

        loginText.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.button_Create)

        registerButton.setOnClickListener {
            performSignUp()
        }
    }


    private fun performSignUp(){

        //showProgressDialog()

        var email = findViewById<EditText>(R.id.editText_Email)
        var password = findViewById<EditText>(R.id.editText_Password)
        var primeiroNome = findViewById<EditText>(R.id.editText_primeiroNome)
        var ultimoNome = findViewById<EditText>(R.id.editText_ultimoNome)

        if(email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Please fill all the fields",
            Toast.LENGTH_SHORT
            ).show()
        }

        val inputEmail = email.text.toString().trim{ it <= ' '}
        val inputPassword = password.text.toString().trim{ it <= ' '}

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->

                showProgressDialog();

                if (task.isSuccessful) {

                    //aqui vai se guardar os dados do utilizador e colocar uma progress bar
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val userInfo = User(
                        firebaseUser.uid,
                        primeiroNome.text.toString().trim{ it <= ' '},
                        ultimoNome.text.toString().trim{ it <= ' '}
                    )


                    //FirestoreClass().registerUser(this@RegisterActivity,userInfo)

                    val mFireStore = Firebase.firestore

                    mFireStore.collection("users")
                        .document(userInfo.id)
                        .set(userInfo, SetOptions.merge())
                        //.add(userInfo)
                        .addOnSuccessListener {
                            userResgistrationSucess()
                            hideProgressDialog()
                        }
                        .addOnFailureListener { e ->

                            hideProgressDialog()
                            Log.e(
                                javaClass.simpleName,
                                "Error while registering the user.",
                                e
                            )

                            Toast.makeText(baseContext ,"deu erro",
                                Toast.LENGTH_SHORT
                            ).show()

                        }


                    // Sign in success, agora vamos para a proxima activity
                    //val intent = Intent(this, MainActivity::class.java)
                    //startActivity(intent)

                    //Toast.makeText(baseContext, "Authentication Sucess.",
                    //    Toast.LENGTH_SHORT
                    //).show()

                } else {

                    hideProgressDialog()
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener{
                hideProgressDialog()
                Toast.makeText(this,"Error occured ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun userResgistrationSucess(){

        //Hide the progress bar
        //hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_sucess),
            Toast.LENGTH_SHORT).show()
    }

}