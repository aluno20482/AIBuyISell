package com.example.projeto.fragment.shopping.categories


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projeto.R
import com.example.projeto.activities.LoginActivity
import com.example.projeto.activities.EditProfileActivity
import com.example.projeto.databinding.FragmentProfileBinding
import com.example.projeto.utils.Constants
import com.google.firebase.auth.FirebaseAuth

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

    }


    //vista para mostar dados do ultizador
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostarDadosUtilizador()


        //Ação do botão editar dados
        val editarDadosButton = binding.buttonEditarDadosPerfil
        editarDadosButton.setOnClickListener {
            //Metedo ir  para a tela
            IrParaJanelaEditarDados()
        }

        //Ação do botão Logout
        val loginButton = binding.buttonLogout
        loginButton.setOnClickListener {
            performLogout()
        }
    }


    //função para mostrar dados do utlizador que tem o login efetuado
    private fun mostarDadosUtilizador() {

        val sharedPreferences = this.requireActivity()
            .getSharedPreferences(Constants.MINHALOJA, AppCompatActivity.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        val telefone = sharedPreferences.getString(Constants.LOGGED_IN_CONTACTO, "")!!
        val morada = sharedPreferences.getString(Constants.LOGGED_IN_MORADA, "")!!

        binding.fragProfileMostrarNomeCompleto.text = "Nome: $username"
        binding.fragProfileMostrarContacto.text = "Telefone:  $telefone"
        binding.fragProfileNMostrarMorada.text = "Morada: $morada"

    }

    private fun IrParaJanelaEditarDados() {
        //passar contexto + class
        val JanelaEditar = Intent(activity, EditProfileActivity::class.java)
        startActivity(JanelaEditar)

    }

    //logou da firabase juntamente com a limpeza da pilha de activities/fragments para ninguem conseguir voltar a entrar na app
    //sem estar devidamente logado
    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
     }
}