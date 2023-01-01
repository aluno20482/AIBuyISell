package com.example.projeto.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.databinding.ActivityAdditemBinding
import com.example.projeto.models.Product
import com.example.projeto.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class AddItemActivity : AppCompatActivity() {

    private val binding by lazy {ActivityAdditemBinding.inflate(layoutInflater) }
    val mFireStore = FirebaseFirestore.getInstance()

    private val selectedImages = mutableListOf<Uri>()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid // Get the user's ID
    val storage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageRef : StorageReference = storage.reference
    var cat = ""

    companion object{
         val IMAGE_REQUEST_CODE=100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //categorias
        mostraCategorias()

        val selecImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data

                    val imageUri = intent?.data
                    imageUri?.let {
                        selectedImages.add(it)
                    }
                }
                //fazer o update do layout depois de obter as imagens
                updateImages()
            }

        //ação do botão para abrir a galeria
        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type="image/"
            selecImagesActivityResult.launch(intent)
        }
        binding.saveProduct.setOnClickListener{
            saveProduct()
        }


        //Botao Registar Redireciona para a pagina de registo
        binding.buttonTirarFoto.setOnClickListener {
            val intent = Intent(this, CamaraActivity::class.java)
            startActivity(intent)
        }



    }

    fun mostraCategorias() : String{
        val categoria = ""
        binding.buttonColorPicker.setOnClickListener {
            val popupMenu = PopupMenu(this,it)
            popupMenu.setOnMenuItemClickListener{ item ->
                when (item.itemId){

                    R.id.menu_option2 -> {
                        Toast.makeText(this, "Telemóveis e Tablets",Toast.LENGTH_SHORT).show()
                        cat = "Telemóveis"
                        true
                    }
                    R.id.menu_option3  -> {
                        Toast.makeText(this, "Decoração",Toast.LENGTH_SHORT).show()
                        //val categoria = R.id.menu_option1.toString()
                        cat = "Decoracao"
                        true
                    }
                    R.id.menu_option4 -> {
                        Toast.makeText(this, "Carros, motas e barcos",Toast.LENGTH_SHORT).show()
                        cat = "Carros"
                        true
                    }
                    R.id.menu_option5 -> {
                        Toast.makeText(this, "Diversos",Toast.LENGTH_SHORT).show()
                        cat = "Diversos"
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.menu_additem)
            popupMenu.show()
        }

        return categoria
    }

    //mostrar a quantidade de imagens que forem selecionadas
    fun updateImages(){
        binding.tvSelectedImages.text = selectedImages.size.toString()
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
            Log.e("INFO", FirebaseAuth.getInstance().currentUser!!.email.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    //guardar o produto na firebase
    private fun saveProduct() {

        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val imgByteArray = getImageToByte()
        val images = mutableListOf<String>()


        //nomenclatura para as instruçoes seguirem a ordem especifica
        lifecycleScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                showProgressBar()
            }

            try{
                    //chamada ao servidor em modo paralelo
                    async {
                        imgByteArray.forEach{
                            val id = UUID.randomUUID().toString()

                            launch {
                                val imageStorage = storageRef.child("/products/images/$id")
                                val result= imageStorage.putBytes(it).await()
                                val downloadedImg = result.storage.downloadUrl.await().toString()


                                images.add(downloadedImg)
                                if(images!=null){
                                    Log.e("INFOIMAGENS:", images.get(0))
                                }
                                Log.e("INFO:",downloadedImg)

                            }
                        }
                    }.await()
                //o await serve para nao se passar para nenhum outro método sem acabar as chamdas ao servidor neste caso
            }catch(e:java.lang.Exception){
                    e.printStackTrace()
                withContext(Dispatchers.Main){
                    hideProgressBar()
                }
            }

            //criar o produto
        val product = Product(
            UUID.randomUUID().toString(),
            name,
            cat,
            price.toFloat(),
            userId,
            userEmail =  FirebaseAuth.getInstance().currentUser!!.email.toString(),
            images
        )

        mFireStore.collection("Products").add(product).addOnSuccessListener {
            hideProgressBar()
        }.addOnFailureListener{
            Log.e("error",it.message.toString())
            hideProgressBar()
        }
        }
    }








    fun getImageToByte():List<ByteArray>{
        val imgByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imageBnp = MediaStore.Images.Media.getBitmap(contentResolver,it)
            if(imageBnp.compress(Bitmap.CompressFormat.JPEG,100,stream)){
                imgByteArray.add(stream.toByteArray())
            }
        }
        return imgByteArray
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
