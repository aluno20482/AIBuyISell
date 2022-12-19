package com.example.projeto.fragment.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projeto.R
import com.example.projeto.databinding.FragmentHomeBinding
import com.example.projeto.adapters.HomeViewPagerAdapter
<<<<<<< HEAD
import com.example.projeto.fragment.categories.*
=======
import com.example.projeto.fragment.shopping.categories.LaptopCategoryFragment
import com.example.projeto.fragment.shopping.categories.MainCategoryFragment
import com.example.projeto.fragment.shopping.categories.PhoneCategoryFragment
import com.example.projeto.fragment.shopping.categories.RamCategoryFragment
>>>>>>> 21940eb3a240a1c5bb594133341fbc0364095ca7
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

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

        //criar o array de fragmentos populado com as categorias, categorias estas que sao fragmentos
        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            SportCategoryFragment(),
            PhoneCategoryFragment(),
            LaptopCategoryFragment(),
            RamCategoryFragment(),

        )

        //utilização do adaptador HomeViewPagerAdapter
        //passsamos o adaptador para uma variavel e fazemos o binding com essa variavel
        val viewPagerToAdapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPagerToAdapter
        //nao permite fazer scroll dentro do viewPager, apenas através do TabLayout
        binding.viewpagerHome.isUserInputEnabled = false
        //binding.viewpagerHome.beginFakeDrag();
        //aqui vai ser feito o switch entre os fragmentos
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Telemóveis"
                2 -> tab.text = "Desporto"
                3 -> tab.text = "Carros"
                4 -> tab.text = "Lazer"
                5 -> tab.text = "Carros"
            }
        }.attach()
        //ligação do tablayout com a viewPage
    }
}