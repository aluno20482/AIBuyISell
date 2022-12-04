package com.example.projeto.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.projeto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


open class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mProgressDialog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val loginButton = findViewById<Button>(R.id.button_Logout)

        loginButton.setOnClickListener {
            performLogout()
        }


        val editarDadosButton = findViewById<Button>(R.id.button_Editar_Dados_Perfil)
        editarDadosButton.setOnClickListener {
            //Metedo ir  para a tela
            IrParaJanelaEditarDados()
        }

    }


    private fun performLogout(){

        FirebaseAuth.getInstance().signOut();
<<<<<<< HEAD:app/src/main/java/com/example/projeto/activities/MainActivity.kt

        //Logout success, voltar para a activity do login
=======
        // Sign in success, agora vamos para a proxima activity
>>>>>>> 566006dcc4f7a6f7b3108534f0de71196a34988d:app/src/main/java/com/example/projeto/MainActivity.kt
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        Toast.makeText(baseContext, "Logout Sucess.",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun IrParaJanelaEditarDados(){


        //passar contexto + class
        val JanelaEditar = Intent(this,UserProfileActivity ::class.java)
        startActivity(JanelaEditar)

        Toast.makeText(baseContext, "Edite os seus dados.",
            Toast.LENGTH_SHORT
        ).show()
    }







    fun showProgressDialog(){
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
        //mProgressDialog.tv_progress_text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()

    }


    //aparecer nome do user quando faz login

    /*
    val sharedPreferences = getSharedPreferences(Constants.MINHALOJA, MODE_PRIVATE)
    val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

    //Colocar para se poder usar o  id do text view

    val principalTXT = findViewById<TextView>(R.id.tv_main_mostrar_nome)


    //val bbb: TextView = findViewById(R.id.tv_main_mostrar_nome)

    //Mostar Nome ao inciar app
    principalTXT. = "Ola  $username."
*/

}