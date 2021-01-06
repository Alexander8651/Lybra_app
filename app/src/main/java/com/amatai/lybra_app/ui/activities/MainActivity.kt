package com.amatai.lybra_app.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.amatai.lybra_app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration:AppBarConfiguration
    lateinit var navController: NavController
    lateinit var drawer:DrawerLayout

    companion object{
        var context:Context? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

}