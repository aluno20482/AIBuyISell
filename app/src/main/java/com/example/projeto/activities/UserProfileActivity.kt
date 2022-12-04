package com.example.projeto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.projeto.R

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.projeto.firestore.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //Botao editar dados do utlizador na firebase
        val guardar: TextView = findViewById(R.id.user_profile_button_Editar_Dados)
        guardar.setOnClickListener{

            //Passar  valor introduzido no  editText_primeiroNome_user_bd
            val firstNameText : EditText = findViewById(R.id.editText_primeiroNome_user_bd)
            val firstNameTextValue = firstNameText.text.toString();  //converter para string

            //Passar  valor introduzido no  editText_ultimoNome_user_bd
            val lastNameText : EditText = findViewById(R.id.editText_ultimoNome_user_bd)
            val lastNameTextValue = lastNameText.text.toString();  //converter para string

            //Passar  valor introduzido no  editText_telemovel_user_bd
            val mobileText : EditText = findViewById(R.id.editText_telemovel_user_bd)
            val mobileTextValue = mobileText.text.toString();  //converter para string

            //chamar a função para carregar os dados na BD
            FirestoreClass().alterarDados(this@UserProfileActivity, firstNameTextValue,lastNameTextValue, mobileTextValue)


        }
    }
}