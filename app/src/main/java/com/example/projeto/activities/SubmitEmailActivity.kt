package com.example.projeto.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.projeto.R
import com.example.projeto.databinding.ActivityAdditemBinding
import com.example.projeto.databinding.ActivitySubmitEmailBinding
import com.example.projeto.utils.Constants
import kotlin.math.log

class SubmitEmailActivity : AppCompatActivity() {

    //usar o bidding
    lateinit var binding: ActivitySubmitEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //usar o bidding
        binding = ActivitySubmitEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val intent = intent
        //val userEmail = intent.getStringExtra("email")

        val preferences = this
            .getSharedPreferences(Constants.USEREMAIL, AppCompatActivity.MODE_PRIVATE)

        val userEmail = preferences.getString("email", "noEmail@gmail.com")
        binding.activitySubmitEmailEmailDestinatario.setText(userEmail.toString())

        //Ao clicar no botão obetem os valores de entrada e chama o metedo enviarEmail
        binding.activitySubmitEmailBotaoEnviarEmail.setOnClickListener {

            val emailDestinatario = binding.activitySubmitEmailEmailDestinatario.text.toString().trim()
            val assunto = binding.activitySubmitEmailEmailAssunto.text.toString().trim()
            val mensagem = binding.activitySubmitEmailEmailMensagem.text.toString().trim()

            //chamadar o método para intenção de enviar e-mail, com os parâmetros de entrada
            enviarEmail(emailDestinatario, assunto, mensagem)
        }
    }


    private fun enviarEmail(emailDestinatario: String, assunto: String, mensagem: String) {

        //ACTION_SEND ação para iniciar um cliente de e-mail instalado em seu dispositivo Android.
        val mIntent = Intent(Intent.ACTION_SEND)

        //Para enviar um e-mail, necessário  mailto: como URI usando o método setData()
        mIntent.data = Uri.parse("mailto:")
        //tipo de dados será text/plain usando o método setType()
        mIntent.type = "text/plain"

        // coloca o emailDestinatario  na intenção
        //destinatário é colocado como array  para se poder enviar a mais que um ultizador  separado por vírgula (,)
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailDestinatario))
        //coloca o assunto na intenção
        mIntent.putExtra(Intent.EXTRA_SUBJECT, assunto)
        //coloca a mensagem no intent
        mIntent.putExtra(Intent.EXTRA_TEXT, mensagem)

        //dados vazios
        if(emailDestinatario.isEmpty() || assunto.isEmpty()  || mensagem.isEmpty() ){
            Toast.makeText(this,"Introduza os dados",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            //inicia intenção de e-mail, mensagem que aparece na inteção
            startActivity(Intent.createChooser(mIntent, "Escolha um cliente de e-mail..."))
        }

    }
}