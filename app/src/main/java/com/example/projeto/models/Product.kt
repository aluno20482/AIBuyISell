package com.example.projeto.models

data class Product(
    val Id: String,
    val name: String,
    val category: String,
    val price: Float,
    val userID: String,
    val userEmail: String,
    //val offerPercentage: Float? = null,
    //val description: String? = null,
    //val colors: List<String>? = null,
    //val sizes: List<String>? = null,
    val images: List<String>?

){

    constructor():this("1","","",0f,"1","1@gmail.com",listOf("1"))
}