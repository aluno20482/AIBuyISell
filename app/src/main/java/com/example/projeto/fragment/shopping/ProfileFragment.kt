package com.example.projeto.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.Fragment
import com.example.projeto.R
import com.example.projeto.databinding.FragmentHomeBinding
import com.example.projeto.utils.Constants

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    //criar o binding para a interagir coma  interface
    private lateinit var  binding : FragmentHomeBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUserData()

    }


    //mostrar dados do utlizador
    private fun displayUserData(){

        val sharedPreferences = this.requireActivity().getSharedPreferences(Constants.MINHALOJA, AppCompatActivity.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!


       // binding.frag_profile_mostrar_nome.text ="Bem vindo $username"

    }

    }