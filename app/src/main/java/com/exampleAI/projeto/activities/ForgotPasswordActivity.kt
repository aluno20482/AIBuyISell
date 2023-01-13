package com.exampleAI.projeto.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.exampleAI.projeto.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        //botao para submeter email
        val btn_enviar_email: TextView = findViewById(R.id.button_Recuperar_Pass)
        btn_enviar_email.setOnClickListener {


            var email = findViewById<EditText>(R.id.email_forgot_pw)
            val inputEmail = email.text.toString().trim { it <= ' ' }
            if (inputEmail.isEmpty()) {
                showErrorSnackBar(
                    "Introduza os dados",
                    true
                )
            } else {

                FirebaseAuth.getInstance().sendPasswordResetEmail(inputEmail)
                    .addOnCompleteListener { task ->
                        //hideProgressDialog()
                        //Sucesso mostra mesagem
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_sucess),
                                Toast.LENGTH_LONG
                            ).show()
                            //terminar a atividade
                            finish()
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Ainda nao é nosso cliente, por favor registe-se!",
                                Toast.LENGTH_LONG
                            ).show()
                            //showErrorSnackBar(task.exception!!.message.toString(), true)
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Introduza um email registado na AI",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    /**Mostra msg de erro*/
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        //se tiver um erro, mostra a cor erro
        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@ForgotPasswordActivity,
                    R.color.colorSnackBarError
                )
            )
            //senão tiver  erro, mostra a cor de sucesso
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@ForgotPasswordActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }
}