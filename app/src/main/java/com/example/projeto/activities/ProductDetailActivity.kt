package com.example.projeto.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.projeto.R
import com.example.projeto.adapters.ProductAdapter
import com.example.projeto.adapters.ViewPagerAdapter
import com.example.projeto.utils.Resource
import com.example.projeto.viewmodel.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ProductDetailActivity() : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList : List<Uri>
    val userId = FirebaseAuth.getInstance().currentUser!!.uid // Get the user's ID
    val db = FirebaseFirestore.getInstance()
    private lateinit var ProductAdapter : ProductAdapter
    private val viewModel by viewModels<FavoritesViewModel>()



    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
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

        ProductAdapter = ProductAdapter()

        btnAdicionaFavoritos.setOnClickListener{
            if (product_name != null) {
                adFavs(product_name)
                lifecycleScope.launchWhenStarted {
                    viewModel.normalProducts.collectLatest {
                        ProductAdapter.differ.submitList(it.data)
                        ProductAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        btnRemoverFavoritos.setOnClickListener{
            if (product_name != null) {
                removeFavs(product_name)
                lifecycleScope.launchWhenStarted {
                    viewModel.normalProducts.collectLatest {
                        ProductAdapter.differ.submitList(it.data)
                        ProductAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        btn_contactarVendedor.setOnClickListener{
            val intent = Intent(this, SubmitEmailActivity::class.java)
            startActivity(intent)
        }
    }

    //Adicona aos favoritos
    fun adFavs(product_name : String){

        val product_price = intent.getStringExtra("price")

        val favorite = hashMapOf(
            "userId" to userId,
            "name" to product_name,
            "images" to imageList,
            "price" to product_price?.toFloat()
        )


        val collectionRef = db.collection("favorites")

        val query = collectionRef
            .whereEqualTo("name", product_name)
            .whereEqualTo("userId", userId)


        val querySnapshot = query.get().addOnCompleteListener {  task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if (querySnapshot.size() > 0) {

                    //Produto já existe nos favoritos
                    Toast.makeText(this, "Este produto já foi adicionado aos favoritos", Toast.LENGTH_SHORT).show()
                } else {
                    //Produto não existe, podemos adicionar
                    FirebaseFirestore.getInstance().collection("favorites").add(favorite)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "Produto Adicionado aos favoritos", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Problema a adicionar produto aos favoritos", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

    }

    //Adicona aos favoritos
    fun removeFavs(product_name : String){
        val collectionRef = db.collection("favorites")
        val query = collectionRef
            .whereEqualTo("name", product_name)
            .whereEqualTo("userId", userId).limit(1)
        val querySnapshot = query.get().addOnCompleteListener {  task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if(querySnapshot.documents.isNotEmpty()){
                    val document = querySnapshot.documents[0]
                    document.reference.delete()
                    Toast.makeText(this, "Produto Removido dos favoritos", Toast.LENGTH_SHORT).show()


                }
            }
        }
    }
}



