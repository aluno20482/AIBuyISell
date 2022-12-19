package com.example.projeto.fragment.shopping

import android.content.Context
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

    private lateinit var  binding : FragmentProfileBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUserData()
    }
    private fun displayUserData(){

        val sharedPreferences = this.requireActivity().getSharedPreferences(Constants.MINHALOJA, AppCompatActivity.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        binding.textView2.text = "Bem vindo:  $username."
    }
}