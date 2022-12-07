package com.example.projeto.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter (
    private val fragments : List<Fragment>,
    fn: FragmentManager,
    lifecicle: Lifecycle
):FragmentStateAdapter(fn,lifecicle){

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}