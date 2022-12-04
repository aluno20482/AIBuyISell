package com.example.projeto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.projeto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


/*

        //botao para subemeter email
        val pass: TextView = findViewById(R.id.et_email_forgot_pw)

        pass.setOnClickListener{
            val email: String = et_email_forgot_pw.text.toString().trim{ it <= '' }
            // se tiver vazio tem de digitar o email
            if(email.isEmpty()){
                //showErrorSnackBar(resources.getString(androidx.compose.ui.R.string.default_error_message), true)
            }else{
                    //showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        //hideProgressDialog()
                        //Sucesso mostra mesagem
                        if (task.isSuccessful){

                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_sucess),
                                Toast.LENGTH_LONG
                            ).show()

                            //terminar a atividade
                            finish()
                        }else{
                            //showErrorSnackBar(task.exception!!.message.toString(), true)
                        }


                    }
            }

        }

*/




    }
}