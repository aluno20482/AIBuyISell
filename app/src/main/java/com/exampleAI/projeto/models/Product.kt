package com.exampleAI.projeto.models

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val userID: String,
    val userEmail: String,
    val description: String? = null,
    val marca: String? = null,
    val images: List<String>?
){
    constructor():this("4","","",0f,"1","1@gmail.com","","",listOf("1"))
}