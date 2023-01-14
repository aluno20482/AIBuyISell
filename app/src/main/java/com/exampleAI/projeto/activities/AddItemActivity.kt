package com.exampleAI.projeto.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.exampleAI.projeto.R
import com.exampleAI.projeto.databinding.ActivityAdditemBinding
import com.exampleAI.projeto.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*


//video auxiliar da classe, mas completado com mais funções
//https://www.youtube.com/watch?v=xk1BKoJ8Nk4&list=PLzZEuVaFb9ExqUwxMoXg0Li0wYW2IeAkz&index=15
@AndroidEntryPoint
class AddItemActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAdditemBinding.inflate(layoutInflater) }
    private val mFireStore = FirebaseFirestore.getInstance()
    private val selectedImages = mutableListOf<Uri>()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid // Get the user's ID
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference
    var cat = ""
    lateinit var name : String
    lateinit var marca : String
    lateinit var desc : String
    lateinit var price : String


    companion object {
        const val IMAGE_REQUEST_CODE = 1
    }

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        //criação do menu de categorias
        menuCategorias()

        //adicionar a barra manualmente a esta atividade visto que nao herda a barra como os framentos adicionados ao slidermenu
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);


        val selecImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    //aqui tratamos o caso em que o utilizador enviou mais que uma foto
                    //clipdata-> coleçao de fotos
                    if (intent?.clipData != null) {
                        for (i in 0 until intent.clipData!!.itemCount) {
                            val imageUri = intent.clipData!!.getItemAt(i).uri
                            selectedImages.add(imageUri)
                        }
                        // apenas uma foto
                        //data-> apenas um elemento
                    } else if (intent?.data != null) {
                        val imageUri = intent.data
                        if (imageUri != null) {
                            selectedImages.add(imageUri)
                        }
                    }
                }
                //fazer o update do layout depois de obter as imagens
                updateImages()
            }

        //ação do botão para abrir a galeria
        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            selecImagesActivityResult.launch(intent)
        }


        binding.saveProduct.setOnClickListener {
            name = binding.edName.text.toString().trim()
            marca = binding.edMarca.text.toString().trim()
            desc = binding.edDesc.text.toString().trim()
            price = binding.edPrice.text.toString().trim()

            if(verificarCampos()) {
                saveProduct()

            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }

        //Tirar fotos
        binding.buttonTirarFoto.setOnClickListener {
            val intent = Intent(this, CamaraActivity::class.java)
            startActivityForResult(intent, IMAGE_REQUEST_CODE)

        }
    }


    /**
     * apagar campos de descrição do produto após guardar o mesmo
     * */
    private fun clearFields() {
        binding.edName.text.clear()
        binding.edMarca.text.clear()
        binding.edDesc.text.clear()
        binding.edPrice.text.clear()
        selectedImages.clear()
        updateImages()
    }


        /**permite voltar para o fragment anterior*/
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    fun  verificarCampos(): Boolean{
        if (name.isEmpty() || desc.isEmpty()  || price.isEmpty() || cat.isEmpty())
            return false

        else if(selectedImages.size == 0)
            return false

        return true
    }

    //ao lancar a atividade da camara, este metodo vai ficar a espera do resultado da mesma
    // e vai assim procurar o valor que vai no intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the Uri of the image from the Intent
            val imageUri = data?.getStringExtra("Uri")
            if (imageUri != null) {
                selectedImages.add(imageUri.toUri())
            }
            updateImages()
        }
    }

    /**
     * função que permite ao utilizador escolher uma categoria para o produto
     */
    fun menuCategorias(): String {
        val categoria = ""
        binding.buttonCategoryPicker.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.menu_option2 -> {
                        Toast.makeText(this, "Telemóveis e Tablets", Toast.LENGTH_SHORT).show()
                        cat = "Telemóveis"
                        true
                    }
                    R.id.menu_option3 -> {
                        Toast.makeText(this, "Decoração", Toast.LENGTH_SHORT).show()
                        //val categoria = R.id.menu_option1.toString()
                        cat = "Decoracao"
                        true
                    }
                    R.id.menu_option4 -> {
                        Toast.makeText(this, "Carros, motas e barcos", Toast.LENGTH_SHORT).show()
                        cat = "Carros"
                        true
                    }
                    R.id.menu_option5 -> {
                        Toast.makeText(this, "Diversos", Toast.LENGTH_SHORT).show()
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

    /**
     * mostrar a quantidade de imagens que forem selecionadas
     */
    fun updateImages() {
        binding.tvSelectedImages.text = selectedImages.size.toString()
    }

    /**
     * guardar o produto na firebase
     */
    private fun saveProduct() {

        val imgByteArray = getImageToByte()
        val images = mutableListOf<String>()

        //nomenclatura para as instruçoes seguirem a ordem especifica
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showProgressBar()
                binding.saveProduct.isEnabled = false
            }

            try {
                //chamada ao servidor em modo paralelo
                async {
                    imgByteArray.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imageStorage = storageRef.child("/products/images/$id")
                            val result = imageStorage.putBytes(it).await()
                            val downloadedImg = result.storage.downloadUrl.await().toString()
                            images.add(downloadedImg)
                        }
                    }
                }.await()
                //o await serve para nao se passar para nenhum outro método sem acabar as chamdas ao servidor neste caso
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
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
                userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString(),
                desc,
                marca,
                images
            )

            mFireStore.collection("Products").add(product).addOnSuccessListener {
                hideProgressBar()
                binding.saveProduct.isEnabled = true
                Toast.makeText(
                    this@AddItemActivity,
                    "Produto colocado para venda!",
                    Toast.LENGTH_SHORT
                ).show()
                //limpar campos
                clearFields()

            }.addOnFailureListener {
                Log.e("error", it.message.toString())
                hideProgressBar()
                binding.saveProduct.isEnabled = true
                Toast.makeText(
                    this@AddItemActivity,
                    "Ocorreu um erro, tente mais tarde!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * função que transforma a imagem em bytes para permitir guardar a mesma na firebase
     * */
    fun getImageToByte(): List<ByteArray> {
        val imgByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach {

            val stream = ByteArrayOutputStream()
            val imageBnp = MediaStore.Images.Media.getBitmap(contentResolver, it)
            if (imageBnp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                imgByteArray.add(stream.toByteArray())
            }
        }
        return imgByteArray
    }

    /**
     * mostra barra de loading
     * */
    fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * esconde barra de loading
     * */
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
