package com.example.projeto.fragment.shopping.categories

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
import com.example.projeto.R
import com.example.projeto.adapters.ProductAdapter
import com.example.projeto.databinding.FragmentArtigosVendaBinding
import com.example.projeto.utils.Resource
import com.example.projeto.viewmodel.CarrosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CarrosFragment : Fragment(R.layout.fragment_carros) {


        private lateinit var binding : FragmentArtigosVendaBinding
        private lateinit var ProductAdapter : ProductAdapter

        private val viewModel by viewModels<CarrosViewModel>()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentArtigosVendaBinding.inflate(inflater)
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

            binding.mainCategoryPB.visibility = View.VISIBLE
        }

        fun  hideLoading(){
            binding.mainCategoryPB.visibility = View.INVISIBLE
        }


        //preparar o layout para receber os dados (layoutManager + adapter)
        private fun setupProductRv() {
            ProductAdapter = ProductAdapter()
            binding.rMelhoresOportunidades.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = ProductAdapter
            }

        }
}
    