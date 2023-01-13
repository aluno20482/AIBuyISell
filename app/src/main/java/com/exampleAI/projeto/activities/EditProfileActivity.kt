package com.exampleAI.projeto.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exampleAI.projeto.R
import android.widget.TextView
import android.widget.Toast
import com.exampleAI.projeto.databinding.ActivityUserProfileBinding
import com.exampleAI.projeto.firestore.FirestoreClass
import com.exampleAI.projeto.utils.Constants.MINHALOJA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditProfileActivity : AppCompatActivity() {

    lateinit var primeiroNomeTXT: TextView
    lateinit var ultimoNomeTXT: TextView
    lateinit var telefoneTXT: TextView
    lateinit var moradaTXT: TextView
    lateinit var userId: String

    private val binding by lazy { ActivityUserProfileBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Obtém o ID do utlizador com login efetuado
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val guardar: TextView = findViewById(R.id.user_profile_button_Editar_Dados)
        //Colocar para se poder usar o  id do text view
        primeiroNomeTXT = findViewById(R.id.editText_primeiroNome_user_bd)
        ultimoNomeTXT = findViewById(R.id.editText_ultimoNome_user_bd)
        telefoneTXT = findViewById(R.id.editText_telemovel_user_bd)
        moradaTXT = findViewById(R.id.editText_morada_user_bd)

        //Botao para editar dados do utlizador na firebase

        guardar.setOnClickListener {

            // verifica se existem dados introduzidos
            if (primeiroNomeTXT.text.isEmpty() || ultimoNomeTXT.text.isEmpty() || telefoneTXT.text.isEmpty() || moradaTXT.text.isEmpty()) {
                Toast.makeText(
                    this, "Por favor preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener   //Retornar sem realizar a atualização
            }

            //Verifica se o campo do telefone tem 9 dígitos
            val mobileRegex = Regex("^[0-9]{9}$")
            val mobileTextValue = telefoneTXT.text.toString()
            if (!mobileRegex.matches(mobileTextValue)) {
                Toast.makeText(
                    this,
                    "Por favor introduza um número de telemóvel válido com 9 dígitos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener   //Retornar sem realizar a atualização
            }

            //Passar  valor introduzido no  editText_primeiroNome_user_bd
            val firstNameTextValue = primeiroNomeTXT.text.toString();  //converter para string
            //Passar  valor introduzido no  editText_ultimoNome_user_bd
            val lastNameTextValue = ultimoNomeTXT.text.toString();  //converter para string
            //Passar  valor introduzido no  editText_morada_user_bd
            val addressTextValue = moradaTXT.text.toString();  //converter para string

            //chamar a função para carregar os dados na BD
            FirestoreClass().alterarDados(
                this@EditProfileActivity,
                firstNameTextValue,
                lastNameTextValue,
                mobileTextValue,
                addressTextValue
            )

            Toast.makeText(
                baseContext, "Dados alterados .",
                Toast.LENGTH_SHORT
            ).show()

        }

        displayUserData()
        //adicionar a barra manualmente a esta atividade visto que nao herda a barra como os framentos adicionados ao slidermenu
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Mostra os dados do utilizador logado*/
    private fun displayUserData() {

        /*val primeiro = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME_FRIST, "")!!
        val ultimo = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME_LAST, "")!!
        val telefone = sharedPreferences.getString(Constants.LOGGED_IN_CONTACTO, "")!!
        val morada = sharedPreferences.getString(Constants.LOGGED_IN_MORADA, "")!!

        //Mostar Nome ao inciar app
        primeiroNomeTXT.text = "$primeiro"
        ultimoNomeTXT.text = "$ultimo"
        telefoneTXT.text = "$telefone"
        moradaTXT.text = "$morada"*/


        // Recupera os dados do utlizador na base de dados
        Firebase.firestore.collection("users").document(userId).get()
            .addOnSuccessListener {
                //Define os valores das exibições EditText com os dados recuperados
                primeiroNomeTXT.text = it.getString("firstName")
                ultimoNomeTXT.text = it.getString("lastName")
                telefoneTXT.text = it.getString("mobile")
                moradaTXT.text = it.getString("address")

                val sharedPreferences = getSharedPreferences(MINHALOJA, MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("logged_in_username_frist", it.getString("firstName"))
                editor.putString("logged_in_username_last", it.getString("lastName"))
                editor.putString("logged_in_contacto", it.getString("mobile"))
                editor.putString("logged_in_morada", it.getString("address"))
                editor.apply()
            }
            .addOnFailureListener {
                //Caso tenha uma falha
                Toast.makeText(this, "Falha ao recuperar os dados", Toast.LENGTH_SHORT).show()
            }
    }
    /**permite voltar para o fragment anterior*/
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}



