package com.example.projeto.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projeto.activities.ProductDetailActivity
import com.example.projeto.activities.SubmitEmailActivity
import com.example.projeto.databinding.ProductItemBinding
import com.example.projeto.models.Product
import com.example.projeto.utils.Constants


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.SpecialProductViewHolder>() {

    var context: Context? = null
    lateinit var  imagesArray : Array<String>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context

    }


    inner class SpecialProductViewHolder(private val binding: ProductItemBinding):

        RecyclerView.ViewHolder(binding.root){

            fun bind(product: Product){
                binding.apply {
                    //o glide auxiliar na transição das imagens para a view
                    Glide.with(itemView).load(product.images?.get(0)).into(binding.imgProduct)
                    tvName.text = product.name
                    tvPrice.text = product.price.toString() + " €"
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

        //passar os dados para a view do detalhe ao se clicar em qualquer produto. Cada produto está dentro da recyclerview
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            val intent2 = Intent(context, SubmitEmailActivity::class.java)
            intent2.putExtra("email",product.userEmail)
            intent.putExtra("nome", product.name)
            intent.putExtra("image", product.images?.get(0))
            intent.putExtra("price", product.price.toString())


            imagesArray  = product.images?.toTypedArray()!!
            intent.putExtra("imagens", imagesArray)

            context!!.startActivity(intent2)

            val preferences = context!!
                .getSharedPreferences(Constants.USEREMAIL, AppCompatActivity.MODE_PRIVATE)

            val editor = preferences.edit()
            editor.putString("email", product.userEmail)
            editor.apply()
        }

    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

}


