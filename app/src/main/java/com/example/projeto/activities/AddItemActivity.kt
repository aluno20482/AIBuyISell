package com.example.projeto.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.projeto.R
import com.example.projeto.databinding.ActivityAdditemBinding
import com.example.projeto.models.Product
import com.google.firebase.firestore.FirebaseFirestore

class AddItemActivity : AppCompatActivity() {

    private val binding by lazy {ActivityAdditemBinding.inflate(layoutInflater) }
    //private val productStorage = Firebase.storage.reference
    val mFireStore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveProduct) {
            val productValidate = validate()
            if (!productValidate) {
                Toast.makeText(this, "Input inválido", Toast.LENGTH_SHORT).show()
                return false
            }
            saveProduct()
        }
        return super.onOptionsItemSelected(item)
    }

    //guardar o produto na firebase
    private fun saveProduct() {


        showProgressBar()

        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val product = Product(
            1,
            name,
            category,
            price.toFloat(),
            5
        )
        mFireStore.collection("Products").add(product).addOnSuccessListener {
            hideProgressBar()
        }.addOnFailureListener{
            Log.e("error",it.message.toString())
            hideProgressBar()
        }

    }

    fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }


    //validar inputs na criacao do produto
    fun validate(): Boolean {
        if (binding.edPrice.text.toString().trim().isEmpty())
            return false

        return true
    }


}
