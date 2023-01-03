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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.projeto.R
import com.example.projeto.adapters.ProductAdapter
import com.example.projeto.adapters.ViewPagerAdapter
import com.example.projeto.fragment.categories.FavoritesFragment
import com.example.projeto.fragment.shopping.categories.ArtigosVendaFragment
import com.example.projeto.viewmodel.ArtigosVendaViewModel
import com.example.projeto.viewmodel.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class ProductDetailActivity() : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList : List<Uri>
    val userId = FirebaseAuth.getInstance().currentUser!!.uid // Get the user's ID
    val db = FirebaseFirestore.getInstance()
    private lateinit var ProductAdapter : ProductAdapter
    private val viewModel by viewModels<FavoritesViewModel>()
    private val viewModelP by viewModels<ArtigosVendaViewModel>()


    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intent = intent

        val productId = intent.getStringExtra("ProductId")
        val product_name = intent.getStringExtra("nome")
        val product_price = intent.getStringExtra("price")
        val product_images = intent.getStringArrayExtra("imagens") as? Array<String>
        val product_marca = intent.getStringExtra("marca")
        val product_desc = intent.getStringExtra("descricao")


        val product_name_view = findViewById<TextView>(R.id.tv_name_detail)
        val product_price_view = findViewById<TextView>(R.id.tv_elevation_detail)
        val btnAdicionaFavoritos = findViewById<Button>(R.id.btn_adicionarFavoritos)
        val btnApagar = findViewById<Button>(R.id.btn_apagar)
        val btnRemoverFavoritos = findViewById<Button>(R.id.btn_removerFavoritos)
        val btn_contactarVendedor = findViewById<Button>(R.id.btn_contactarVendedor)
        val product_marca_view =  findViewById<TextView>(R.id.tv_marca_detail)
        val product_desc_view =  findViewById<TextView>(R.id.tv_desc_detail)
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

        product_desc_view.text= product_desc
        product_marca_view.text = product_marca
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
        CheckIfCanDelete(btnApagar,btnAdicionaFavoritos,btnRemoverFavoritos,btn_contactarVendedor)


        btnApagar.setOnClickListener{
            if (productId != null) {
                Log.e("userId", userId)
                Log.e("Name",product_name.toString())
                deleteProductFromDatabase(productId)
            }
        }


        btnRemoverFavoritos.setOnClickListener{
            if (product_name != null) {
                removeFavs(product_name)
                lifecycleScope.launchWhenStarted {
                    viewModel.normalProducts.collectLatest {
                        ProductAdapter.notifyDataSetChanged()
                        ProductAdapter.differ.submitList(it.data)

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
    @SuppressLint("NotifyDataSetChanged")
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
                            updateRvFavs()

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
                    updateRvFavs()
                }
            }
        }
    }

    fun CheckIfCanDelete(btnDel: Button,btnFavs: Button,bntRem : Button,btnContact:Button){
        val productUserId = intent.getStringExtra("productUser")
        if (productUserId == userId){
            btnDel.isVisible = true
            btnFavs.isVisible = false
            bntRem.isVisible = false
            btnContact.isVisible=false
        }
    }

    private fun deleteProductFromDatabase(productId: String) {
        val collectionRef = db.collection("Products")
        val query = collectionRef
            .whereEqualTo("id", productId)
        val querySnapshot = query.get().addOnCompleteListener {  task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if(querySnapshot.documents.isNotEmpty()){
                    val document = querySnapshot.documents[0]
                    document.reference.delete()
                    Toast.makeText(this, "Produto Removido ", Toast.LENGTH_SHORT).show()
                    updateRvVenda()
                }
            }
        }
    }



    fun updateRvFavs(){
        val fragmentManager: FragmentManager = supportFragmentManager
        val newFragment: Fragment = FavoritesFragment()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.containerFavs, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    fun updateRvVenda(){
        val fragmentManager: FragmentManager = supportFragmentManager
        val newFragment: Fragment = ArtigosVendaFragment()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.containerVenda, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}



