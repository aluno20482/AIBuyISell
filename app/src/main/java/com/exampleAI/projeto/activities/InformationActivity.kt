package com.exampleAI.projeto.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.exampleAI.projeto.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay

class InformationActivity : AppCompatActivity() {

    //vars globais para toda a classe
    private lateinit var map: MapView
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)


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
        showMap()
    }

    /**Abre o mapa na atividade*/
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
        var point = GeoPoint(39.60068, -8.38967)  // 39.60199, -8.39675
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

    /**mapa é parado*/
    override fun onPause() {
        super.onPause()
        map.onResume()
    }

    /**resumo mapa*/
    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    /**funcao vai receber um array de strings*/
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
}