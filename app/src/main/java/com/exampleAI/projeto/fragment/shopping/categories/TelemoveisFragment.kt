package com.exampleAI.projeto.fragment.categories

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
import androidx.recyclerview.widget.GridLayoutManager
import com.exampleAI.projeto.adapters.ProductAdapter
import com.exampleAI.projeto.databinding.FragmentTelemoveisBinding
import com.exampleAI.projeto.utils.Resource
import com.exampleAI.projeto.viewmodel.TemeloveisViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TelemoveisFragment : Fragment() {


    private lateinit var binding : FragmentTelemoveisBinding
    private lateinit var ProductAdapter : ProductAdapter

    private val viewModel by viewModels<TemeloveisViewModel>()

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
                        ProductAdapter.differ.submitList(it.data)
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
        ProductAdapter = ProductAdapter()
        binding.rTodosOsProdutos.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = ProductAdapter
        }

    }
}