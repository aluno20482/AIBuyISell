package com.example.projeto.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.projeto.databinding.FragmentMainCategoryBinding
import com.example.projeto.databinding.ProductItemBinding
import com.example.projeto.models.Product

class SpecialProductAdapter : RecyclerView.Adapter<SpecialProductAdapter.SpecialProductViewHolder>(){

    inner class SpecialProductViewHolder(private val binding: ProductItemBinding):
        RecyclerView.ViewHolder(binding.root){

            fun bind(product: Product){
                binding.apply {
                    //o glide auxiliar na transição das imagens para a view
                    Glide.with(itemView).load(product.images?.get(0)).into(binding.imgProduct)
                    tvName.text = product.name
                    tvPrice.text = product.price.toString()
                    tvName.text = product.name

                }
            }
        }

    val diffCallback = object :DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
           return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
        return SpecialProductViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

}


