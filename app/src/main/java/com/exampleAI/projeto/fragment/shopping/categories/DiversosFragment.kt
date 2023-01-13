package com.exampleAI.projeto.fragment.shopping.categories

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
import com.exampleAI.projeto.R
import com.exampleAI.projeto.adapters.ProductAdapter
import com.exampleAI.projeto.databinding.FragmentDiversosBinding
import com.exampleAI.projeto.utils.Resource
import com.exampleAI.projeto.viewmodel.DiversosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DiversosFragment : Fragment(R.layout.fragment_diversos) {

    private lateinit var binding: FragmentDiversosBinding
    private lateinit var ProductAdapter: ProductAdapter

    private val viewModel by viewModels<DiversosViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiversosBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRv()
        lifecycleScope.launchWhenStarted {
            viewModel.normalProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        ProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(ContentValues.TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        val mySwipeRefreshLayout = binding.swiperefresh
        mySwipeRefreshLayout.setOnRefreshListener {
            refreshView()
            mySwipeRefreshLayout.isRefreshing = false
        }
    }

    /** faz o refresh da view com o scrol para baixo*/
    private fun refreshView() {
        setupProductRv()
        viewModel.fetchProducts()
    }


    fun showLoading() {

        binding.diversosCategoryPB.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.diversosCategoryPB.visibility = View.INVISIBLE
    }


    //preparar o layout para receber os dados (layoutManager + adapter)
    private fun setupProductRv() {
        ProductAdapter = ProductAdapter()
        binding.rMelhoresOportunidades.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = ProductAdapter
        }

    }
}
