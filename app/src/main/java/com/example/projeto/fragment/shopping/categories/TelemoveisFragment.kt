package com.example.projeto.fragment.categories

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto.adapters.ProductAdapter
import com.example.projeto.databinding.FragmentTelemoveisBinding
import com.example.projeto.utils.Resource
import com.example.projeto.viewmodel.PhoneCategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TelemoveisFragment : Fragment() {


    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var binding : FragmentTelemoveisBinding
    private lateinit var specialPrductAdapter : ProductAdapter

    private val viewModel by viewModels<PhoneCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTelemoveisBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRv()
        lifecycleScope.launchWhenStarted {
            viewModel.normalProducts.collectLatest {
                when(it){
                    is Resource.Loading->{
                        showLoading()
                    }
                    is Resource.Success->{
                        specialPrductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Log.e(ContentValues.TAG, it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    fun showLoading(){

        binding.sportCategoryPB.visibility =View.VISIBLE
    }

    fun  hideLoading(){
        binding.sportCategoryPB.visibility =View.INVISIBLE
    }


    //preparar o layout para receber os dados (layoutManager + adapter)
    private fun setupProductRv() {
        specialPrductAdapter = ProductAdapter()
        binding.rTodosOsProdutos.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false)
            adapter = specialPrductAdapter
        }

    }
}