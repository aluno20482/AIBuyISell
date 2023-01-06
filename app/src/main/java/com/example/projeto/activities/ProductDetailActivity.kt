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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.projeto.R
import com.example.projeto.adapters.ProductAdapter
import com.example.projeto.adapters.ViewPagerAdapter
import com.example.projeto.fragment.shopping.categories.ArtigosVendaFragment
import com.example.projeto.viewmodel.ArtigosVendaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class ProductDetailActivity() : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList : List<Uri>
    val userId = FirebaseAuth.getInstance().currentUser!!.uid // Get the user's ID
    val db = FirebaseFirestore.getInstance()
    private lateinit var ProductAdapter : ProductAdapter
    private val viewModel by viewModels<ArtigosVendaViewModel>()



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
        val btnApagar = findViewById<Button>(R.id.btn_apagar)
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

        CheckIfCanDelete(btnApagar,btn_contactarVendedor)

        btnApagar.setOnClickListener{
            if (productId != null) {
                deleteProductFromDatabase(productId)

            }
        }

        btn_contactarVendedor.setOnClickListener{
            val intent = Intent(this, SubmitEmailActivity::class.java)
            startActivity(intent)
        }
    }


    /**Verifica se quem está a ver um produto é o seu "criador", se sim o botão de contactar vendedor desaparece
     * e o botão de apagar aparece */
    fun CheckIfCanDelete(btnApagar : Button,btnContact:Button){
        val productUserId = intent.getStringExtra("productUser")
        if (productUserId == userId){
            btnApagar.isVisible = true
            btnContact.isVisible=false
        }
    }

    /**Apaga um produto da base de dados*/
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
                    viewModel.fetchProducts()
                    //updateRvVenda()
                }
            }
        }
    }

    fun updateRvVenda(){
        val newFragment: Fragment =  ArtigosVendaFragment()
        //val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentManager: FragmentManager = Fragment().childFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.nav_host_fragment_content_main, newFragment)
        transaction.addToBackStack(null)
        transaction.commit();
    }
}



