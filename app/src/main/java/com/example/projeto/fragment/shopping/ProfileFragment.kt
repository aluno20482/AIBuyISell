package com.example.projeto.fragment.shopping


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projeto.R
import com.example.projeto.databinding.FragmentProfileBinding
import com.example.projeto.utils.Constants



class ProfileFragment : Fragment(R.layout.fragment_profile) {

    //criar o binding para a interagir coma  interface
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root

/*
        //BOTAO EDITAR DADOS
        val editarDadosButton = binding.buttonEditarDadosPerfil
        editarDadosButton.setOnClickListener {
            //Metedo ir  para a tela
           // IrParaJanelaEditarDados()
        }

        //BOTAO LOGOUT

        val loginButton = binding.buttonLogout
        loginButton.setOnClickListener {
           // performLogout()
        }

*/
    }


    //vista para mostar dados do ultizador
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostarDadosUtilizador()
    }


    //função para mostrar dados do utlizador que tem o login efetuado
    private fun mostarDadosUtilizador() {

        val sharedPreferences = this.requireActivity()
            .getSharedPreferences(Constants.MINHALOJA, AppCompatActivity.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        val telefone = sharedPreferences.getString(Constants.LOGGED_IN_CONTACTO, "")!!
        val morada = sharedPreferences.getString(Constants.LOGGED_IN_MORADA, "")!!

        binding.fragProfileMostrarNomeCompleto.text = "Bem vindo: $username"
        binding.fragProfileMostrarContacto.text = "Telefone:  $telefone"
        binding.fragProfileNMostrarMorada.text = "Minha Morada : $morada"
    }


    /*
    private fun IrParaJanelaEditarDados() {

        //passar contexto + class
        val JanelaEditar = Intent(this, UserProfileActivity::class.java)
        startActivity(JanelaEditar)

    }


    private fun performLogout() {

        FirebaseAuth.getInstance().signOut();
        //Logout success, voltar para a activity do login
        // Sign in success, agora vamos para a proxima activity

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        Toast.makeText(baseContext, "Logout Sucess.",
            Toast.LENGTH_SHORT
        ).show()
    }

     */


}