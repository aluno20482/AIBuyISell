package com.example.projeto.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projeto.R


class ProductDetailActivity() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intent = intent

        val product_name = intent.getStringExtra("nome")
        val product_image = intent.getStringExtra("image")
        val product_price = intent.getStringExtra("price")


        val product_name_view = findViewById<TextView>(R.id.tv_name_detail)
        val product_image_view = findViewById<ImageView>(R.id.img_detail)
        val product_price_view = findViewById<TextView>(R.id.tv_elevation_detail)

        //passar a string para Uri
        val myUri = Uri.parse(product_image)


        product_name_view.text = product_name

        product_price_view.text = product_price.toString() + "€"

        //uso do glide mas pode ser de outra forma para passar a imagem à view
        Glide.with(this)
            .load(myUri)
            .into(product_image_view);

    }

}