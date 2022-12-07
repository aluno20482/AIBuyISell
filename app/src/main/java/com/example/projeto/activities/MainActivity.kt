package com.example.projeto.activities

import android.app.Application
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.projeto.R
import com.example.projeto.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp


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

        displayUserData();


        val editarDadosButton = findViewById<Button>(R.id.button_Editar_Dados_Perfil)
        editarDadosButton.setOnClickListener {
            //Metedo ir  para a tela



        try {

            IrParaJanelaEditarDados()
        }
<<<<<<< HEAD
=======
        catch (ex: Exception )
        {
            Log.i("Primeiro Nome", "user.firstName");
        }
    }


>>>>>>> 6e3a79e5511e8fafdc12fbc4742cf258d964ccb2
    }


    private fun displayUserData(){
        val sharedPreferences = getSharedPreferences(Constants.MINHALOJA, MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        val telefone = sharedPreferences.getString(Constants.LOGGED_IN_CONTACTO, "")!!
        val morada = sharedPreferences.getString(Constants.LOGGED_IN_MORADA, "")!!

        //Colocar para se poder usar o  id do text view
        val principalTXT = findViewById<TextView>(R.id.tv_main_mostrar_nome)
        val telefoneTXT = findViewById<TextView>(R.id.tv_main_mostrar_contacto)
        val moradaTXT = findViewById<TextView>(R.id.tv_main_mostrar_morada)


        //val bbb: TextView = findViewById(R.id.tv_main_mostrar_nome)

        //Mostar Nome ao inciar app
        principalTXT.text = "Bem vindo:  $username."

        //Mostar Nome ao inciar app
        telefoneTXT.text = "Telefone : $telefone."

        //Mostar Nome ao inciar app
        moradaTXT.text = "Morada : $morada."


    }

    private fun performLogout(){

        FirebaseAuth.getInstance().signOut();


        //Logout success, voltar para a activity do login

        // Sign in success, agora vamos para a proxima activity

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        Toast.makeText(baseContext, "Logout Sucess.",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun IrParaJanelaEditarDados(){

        //passar contexto + class
        val JanelaEditar = Intent(this, UserProfileActivity::class.java)
        startActivity(JanelaEditar)

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




}