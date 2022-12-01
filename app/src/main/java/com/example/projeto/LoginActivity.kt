package com.example.projeto

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : MainActivity() {

    private lateinit var auth: FirebaseAuth

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

        loginButton.setOnClickListener {
            showProgressDialog()
            performLogin()

        }
    }
    private fun performLogin(){
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

                }
            }
            .addOnFailureListener{
                Toast.makeText(baseContext,"Authentication failed.${it.localizedMessage}",
                Toast.LENGTH_SHORT).show()
            }

    }
}