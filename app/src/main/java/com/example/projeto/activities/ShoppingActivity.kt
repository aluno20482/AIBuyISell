package com.example.projeto.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projeto.R
import com.example.projeto.databinding.ActivityShoppingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : FragmentActivity() {

    private lateinit var auth: FirebaseAuth

    //criação do bind
    val binding by lazy{
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    //criação da controlador que vai gerir os fragmentos
    val navController = findNavController(R.id.host_fragment)
    binding.bottomNavigation.setupWithNavController(navController)
    }
}