package com.example.projeto.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.projeto.R
import com.example.projeto.adapters.ViewPagerAdapter
import com.example.projeto.models.Product
import com.example.projeto.utils.Constants
import com.example.projeto.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


class ProductDetailActivity() : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList : List<Uri>



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intent = intent


        val product_name = intent.getStringExtra("nome")
        val product_price = intent.getStringExtra("price")
        val product_images = intent.getStringArrayExtra("imagens") as? Array<String>


        val product_name_view = findViewById<TextView>(R.id.tv_name_detail)
        val product_price_view = findViewById<TextView>(R.id.tv_elevation_detail)
        val btnAdicionaFavoritos = findViewById<Button>(R.id.btn_adicionarFavoritos)
        val btnRemoverFavoritos = findViewById<Button>(R.id.btn_removerFavoritos)
        val btn_contactarVendedor = findViewById<Button>(R.id.btn_contactarVendedor)
        viewPager = findViewById(R.id.idViewPager)

        //fazer o parse para Uri do array de strings que vem do intent do productAdapter
        imageList = arrayListOf()
        if (product_images != null) {
            for(img in product_images){
                val myUri = Uri.parse(img)
                imageList = imageList + myUri
            }
        }

        //iniciar o viewPager e passar a lista de imagens
        viewPagerAdapter = ViewPagerAdapter(this, imageList)


        product_name_view.text = product_name
        product_price_view.text = product_price.toString() + "€"
        viewPager.adapter = viewPagerAdapter

        btnAdicionaFavoritos.setOnClickListener{
            if (product_name != null) {
                adFavs(product_name)
                Toast.makeText(this, "Produto Adicionado aos favoritos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRemoverFavoritos.setOnClickListener{
            if (product_name != null) {
               removeFavs(product_name)
                Toast.makeText(this, "Produto Removido aos favoritos", Toast.LENGTH_SHORT).show()
            }
        }



        btn_contactarVendedor.setOnClickListener{
            val intent = Intent(this, SubmitEmailActivity::class.java)
            startActivity(intent)
        }
    }


    //Adicona aos favoritos
    fun adFavs(product_name : String){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid // Obter o ID do usuário
        val product = product_name // currentProduct é o produto que o usuário deseja adicionar aos seus favoritos
        val product_name = intent.getStringExtra("nome")
        val product_price = intent.getStringExtra("price")


        val favorite = hashMapOf(
            "userId" to userId,
            "productName" to product,
            "images" to imageList

        )

        FirebaseFirestore.getInstance().collection("favorites").add(favorite)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Favorite product added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding favorite product", e)
            }

    }






    //Adicona aos favoritos
    fun removeFavs(product_name : String){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid // Get the user's ID
        val product = product_name // currentProduct is the product that the user wants to add to their favorites
        val product_name = intent.getStringExtra("nome")
        val product_price = intent.getStringExtra("price")


        val favorite = hashMapOf(
            "userId" to userId,
            "productName" to product,
            "images" to imageList

        )

        //remover artigo clicado
        FirebaseFirestore.getInstance().collection("favorites").document("BOV71XmqSCOtwmLYIwSv")
            .delete()
            .addOnCompleteListener { documentReference ->
                Log.d(TAG, "Sucesso ao Remover")
            }

    }





}