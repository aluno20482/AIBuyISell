package com.example.projeto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.projeto.R

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projeto.firestore.FirestoreClass
import com.example.projeto.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //Botao para editar dados do utlizador na firebase
        val guardar: TextView = findViewById(R.id.user_profile_button_Editar_Dados)

        guardar.setOnClickListener {
            //Declarar variaveis para pode icializar as referências  EditText
            val firstNameText: EditText = findViewById(R.id.editText_primeiroNome_user_bd)
            val lastNameText: EditText = findViewById(R.id.editText_ultimoNome_user_bd)
            val mobileText: EditText = findViewById(R.id.editText_telemovel_user_bd)
            val addressText: EditText = findViewById(R.id.editText_morada_user_bd)


            // verifica se existem dados introduzidos
            if (firstNameText.text.isEmpty() || lastNameText.text.isEmpty() || mobileText.text.isEmpty() || addressText.text.isEmpty()) {
                Toast.makeText(this, "Por favor preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener   //Retornar sem realizar a atualização
            }

            //Verifica se o campo do telefone tem 9 dígitos
            val mobileRegex = Regex("^[0-9]{9}$")
            val mobileTextValue = mobileText.text.toString()
            if (!mobileRegex.matches(mobileTextValue)) {
                Toast.makeText(this,
                    "Por favor introduza um número de telemóvel válido com 9 dígitos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener   //Retornar sem realizar a atualização
            }

            //Passar  valor introduzido no  editText_primeiroNome_user_bd
            val firstNameTextValue = firstNameText.text.toString();  //converter para string
            //Passar  valor introduzido no  editText_ultimoNome_user_bd
            val lastNameTextValue = lastNameText.text.toString();  //converter para string
            //Passar  valor introduzido no  editText_morada_user_bd
            val addressTextValue = addressText.text.toString();  //converter para string

            //chamar a função para carregar os dados na BD
            FirestoreClass().alterarDados(this@UserProfileActivity,
                firstNameTextValue,
                lastNameTextValue,
                mobileTextValue,
                addressTextValue)

            Toast.makeText(baseContext, "Dados alterados .",
                Toast.LENGTH_SHORT
            ).show()

        }
        displayUserData()
    }
    /**
     * Mostra os dados do utilizador logado*/
    private fun displayUserData() {
        val sharedPreferences = getSharedPreferences(Constants.MINHALOJA, MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        val primeiro = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME_FRIST, "")!!
        val ultimo = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME_LAST, "")!!
        val telefone = sharedPreferences.getString(Constants.LOGGED_IN_CONTACTO, "")!!
        val morada = sharedPreferences.getString(Constants.LOGGED_IN_MORADA, "")!!

        //Colocar para se poder usar o  id do text view
        val primeiroNomeTXT = findViewById<TextView>(R.id.editText_primeiroNome_user_bd)
        val ultimoNomeTXT = findViewById<TextView>(R.id.editText_ultimoNome_user_bd)
        val telefoneTXT = findViewById<TextView>(R.id.editText_telemovel_user_bd)
        val moradaTXT = findViewById<TextView>(R.id.editText_morada_user_bd)

        //Mostar Nome ao inciar app
        primeiroNomeTXT.text = "$primeiro"
        ultimoNomeTXT.text = "$ultimo"
        telefoneTXT.text = "$telefone"
        moradaTXT.text = "$morada"

        // Obtém o ID do utlizador com login efetuado
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Recupera os dados do utlizador na base de dados
        Firebase.firestore.collection("users").document(userId).get()
            .addOnSuccessListener {
                //Define os valores das exibições EditText com os dados recuperados
                findViewById<EditText>(R.id.editText_primeiroNome_user_bd).setText(it.getString("firstName"))
                findViewById<EditText>(R.id.editText_ultimoNome_user_bd).setText(it.getString("lastName"))
                findViewById<EditText>(R.id.editText_telemovel_user_bd).setText(it.getString("mobile"))
                findViewById<EditText>(R.id.editText_morada_user_bd).setText(it.getString("address"))
            }
            .addOnFailureListener {
                //Caso tenha uma falha
                Toast.makeText(this, "Falha ao recuperar os dados", Toast.LENGTH_SHORT).show()
            }
    }
}



