package com.example.projeto.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.projeto.R
import com.example.projeto.adapters.ViewPagerAdapter


class ProductDetailActivity() : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList : List<Uri>
    //companion object ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intent = intent

        val product_name = intent.getStringExtra("nome")
        val product_price = intent.getStringExtra("price")
        val product_images = intent.getStringArrayExtra("imagens") as? Array<String>


        val product_name_view = findViewById<TextView>(R.id.tv_name_detail)
        val product_price_view = findViewById<TextView>(R.id.tv_elevation_detail)

        product_name_view.text = product_name
        product_price_view.text = product_price.toString() + "€"

        //passar a string para Uri
        // val product_image = intent.getStringExtra("image")
        // val product_image_view = findViewById<ImageView>(R.id.img_detail)
        //val myUri = Uri.parse(product_image)
        //uso do glide mas pode ser de outra forma para passar a imagem à view
        //Glide.with(this)
         //   .load(myUri)
         //   .into(product_image_view);

        //product_image_view.setImageURI(myUri)

        //fazer o parse para Uri do array de strings que vem do intent do productAdapter
        imageList = arrayListOf()
        if (product_images != null) {
            for(img in product_images){
                val myUri = Uri.parse(img)
                imageList = imageList + myUri
            }
        }

        viewPager = findViewById(R.id.idViewPager)

        //iniciar o viewPager e passar a lista de imagens
        viewPagerAdapter = ViewPagerAdapter(this, imageList)

        viewPager.adapter = viewPagerAdapter





    }

}