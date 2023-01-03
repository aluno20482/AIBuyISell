package com.example.projeto.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.models.Product
import com.example.projeto.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiversosViewModel @Inject constructor (
    private val firestore: FirebaseFirestore
) :ViewModel() {

    private val _normalProducs = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val normalProducts : StateFlow<Resource<List<Product>>> = _normalProducs
    val userId = FirebaseAuth.getInstance().currentUser!!.uid // Obter o ID do usuário

    init {
        fetchProducts()
    }

    fun fetchProducts(){
        viewModelScope.launch {
            _normalProducs.emit(Resource.Loading())
        }
        // adiconar favoritos

        firestore.collection("Products").whereEqualTo("category", "Diversos").whereNotEqualTo("userID",userId )
           .get().addOnSuccessListener { result ->
               //conversao da informaçao da firebase numa lista de objetos de produtos
               val specialProductList = result.toObjects(Product::class.java)
               viewModelScope.launch {
                   _normalProducs.emit(Resource.Success(specialProductList))
               }
           }.addOnFailureListener{
               viewModelScope.launch {
                  _normalProducs.emit(Resource.Error(it.message.toString()))
              }
          }


    }

}