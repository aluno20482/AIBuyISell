package com.example.projeto.models

data class Product(
    val String: Int,
    val name: String,
    val category: String,
    val price: Float,
    val id: Int,
    //val offerPercentage: Float? = null,
    //val description: String? = null,
    //val colors: List<String>? = null,
    //val sizes: List<String>? = null,
    //val images: List<String>
){
    constructor():this(2,"","",0f,1)
}