package com.example.projeto.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projeto.R
import com.example.projeto.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

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

        val categoriesFragments = arrayListOf<Fragment>()
    }
}