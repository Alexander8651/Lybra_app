package com.amatai.lybra_app.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.amatai.lybra_app.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration:AppBarConfiguration
    lateinit var navController: NavController
    lateinit var drawer:DrawerLayout

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationRequest: LocationRequest? = null

    companion object{
        var context:Context? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        context = this

        navController = findNavController(R.id.fragmentController)
        drawer = maindrawer

        NavigationUI.setupActionBarWithNavController(this,navController,drawer)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawer)

        NavigationUI.setupWithNavController(navViewDrawer,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun inicializarLocationRequest() {
        locationRequest = LocationRequest()
        //Solicita la Ubicacion cada 10 segundos
        locationRequest?.interval = 10000
        //Actualiza la ubicacion cada 5 segundos
        locationRequest?.fastestInterval = 5000
        // exactitud con la que se quiere obtener la ubicacion, alta para este caso
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}