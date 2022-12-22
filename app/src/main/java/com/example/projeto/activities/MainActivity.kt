package com.example.projeto.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projeto.R
import com.example.projeto.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

open class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mProgressDialog: Dialog


    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    //var global para toda a class
    //private lateinit var map: MapView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val loginButton = findViewById<Button>(R.id.button_Logout)

        loginButton.setOnClickListener {
            performLogout()
        }

        displayUserData();

        val homeButton = findViewById<Button>(R.id.button_home)

        homeButton.setOnClickListener {
            verHome()

        }
        val addProduct = findViewById<Button>(R.id.button_addProduct)

        addProduct.setOnClickListener {
            verAddProduct()
        }


        val editarDadosButton = findViewById<Button>(R.id.button_Editar_Dados_Perfil)
        editarDadosButton.setOnClickListener {
            //Metedo ir  para a tela

            IrParaJanelaEditarDados()
        }

/*
        //avaliar se a pessoa deu permissão para usar os recursos
        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )

        //adiconar OpenStreetMap to activity
        showMap()*/
    }


    private fun verAddProduct() {
        val intent = Intent(this, AddItemActivity::class.java)
        startActivity(intent)
    }


    private fun verHome() {
        // Sign in success, agora vamos para a proxima activity
        val intent = Intent(this, ShoppingActivity::class.java)
        startActivity(intent)
    }


    private fun displayUserData() {
        val sharedPreferences = getSharedPreferences(Constants.MINHALOJA, MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        val telefone = sharedPreferences.getString(Constants.LOGGED_IN_CONTACTO, "")!!
        val morada = sharedPreferences.getString(Constants.LOGGED_IN_MORADA, "")!!

        //Colocar para se poder usar o  id do text view
        val principalTXT = findViewById<TextView>(R.id.tv_main_mostrar_nome)
        val telefoneTXT = findViewById<TextView>(R.id.tv_main_mostrar_contacto)
        val moradaTXT = findViewById<TextView>(R.id.tv_main_mostrar_morada)


        //val bbb: TextView = findViewById(R.id.tv_main_mostrar_nome)

        //Mostar Nome ao inciar app
        principalTXT.text = "Bem vindo:  $username."

        //Mostar Nome ao inciar app
        telefoneTXT.text = "Telefone : $telefone."

        //Mostar Nome ao inciar app
        moradaTXT.text = "Morada : $morada."


    }

    private fun performLogout() {

        FirebaseAuth.getInstance().signOut();


        //Logout success, voltar para a activity do login

        // Sign in success, agora vamos para a proxima activity

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        Toast.makeText(baseContext, "Logout Sucess.",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun IrParaJanelaEditarDados() {

        //passar contexto + class
        val JanelaEditar = Intent(this, UserProfileActivity::class.java)
        startActivity(JanelaEditar)

    }


    fun showProgressDialog() {
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
        //mProgressDialog.tv_progress_text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()

    }


    /*

    //Abre o mapa na atividade
    private fun showMap() {

        //org -> para configurar o mapa
        //definir apenas um mapa em todo o programa
        Configuration.getInstance().setUserAgentValue(this.getPackageName())


        //vai buscar a view
        map = findViewById(R.id.map)  //erro nao pode ser nulo
        map.setTileSource(TileSourceFactory.MAPNIK)
        //zoom do mapa
        map.controller.zoomTo(17.0)
        //mostra botões de zoom
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true) // para poder fazer zoom com os dedos


        //bosula dentro do mapa
        var compassOverlay = CompassOverlay(this, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

        //ponto 1
        var point = GeoPoint(39.60068, -8.38967)       // 39.60199, -8.39675
        var startMarker = Marker(map)
        startMarker.position = point
        //coloca no centro ao clicar na mão
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

        startMarker.infoWindow = MarkerWindow(map, this, "IPT")

        map.overlays.add(startMarker)


        Handler(Looper.getMainLooper()).postDelayed({
            map.controller.setCenter(point)

        }, 1000) // waits one second to center map
    }


    //mapa e parado
    override fun onPause() {
        super.onPause()
        map.onResume()
    }


    //resumo mapa
    override fun onResume() {
        super.onResume()
        map.onResume()
    }


    //funcao vai receber um array de strings
    private fun requestPermissionsIfNecessary(permissions: Array<out String>) {
        val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOf<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            );
        }


    }
*/

}