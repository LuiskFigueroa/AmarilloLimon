package com.lcarlosfb.amarillolimon.ui.main

import android.os.Bundle

import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.databinding.ActivityNavigationDrawerBinding
//import com.lcarlosfb.amarillolimon.ui.main.databinding.ActivityNavigationDrawerBinding

class NavigationDrawerActivity : AppCompatActivity() {

	private lateinit var appBarConfiguration: AppBarConfiguration
	private lateinit var binding: ActivityNavigationDrawerBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.appBarNavigationDrawer.toolbar)


		val drawerLayout: DrawerLayout = binding.drawerLayout
		val navView: NavigationView = binding.navView
		val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.homeFragment, R.id.favoritesFragment, R.id.cartFragment, R.id.profileFragment
			), drawerLayout
		)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView.setupWithNavController(navController)
	}


	/* override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.navigation_drawer, menu)
		return true
	} */

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}
}

// Laboratorio 4 Ok
