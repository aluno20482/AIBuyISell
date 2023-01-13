package com.exampleAI.projeto.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exampleAI.projeto.activities.ProductDetailActivity
import com.exampleAI.projeto.databinding.ProductItemBinding
import com.exampleAI.projeto.models.Product
import com.exampleAI.projeto.utils.Constants


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.SpecialProductViewHolder>() {

    var context: Context? = null
    lateinit var  imagesArray : Array<String>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    /**Função responsável por popular o ViewHolder com as informações de cada Produto
     * Estes produtos são inseridos dentro do layout "productItem" que por sua vez é inserido dentro de uma recyclerView*/
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
           return oldItem.id == newItem.id
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

            intent.putExtra("nome", product.name)
            intent.putExtra("image", product.images?.get(0))
            intent.putExtra("price", product.price.toString())
            intent.putExtra("descricao",product.description)
            intent.putExtra("marca",product.marca)
            intent.putExtra("ProductId", product.id)
            intent.putExtra("productUser",product.userID)
            intent.putExtra("position", position.toString())

            imagesArray  = product.images?.toTypedArray()!!
            intent.putExtra("imagens", imagesArray)
            //atividade dos detalhes
            context!!.startActivity(intent)

            //shared preferences para passar o email para a "submitEmailActivity"
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


