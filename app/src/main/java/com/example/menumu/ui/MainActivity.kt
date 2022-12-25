package com.example.menumu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.menumu.R
import com.example.menumu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private lateinit var navController: NavController
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navController = findNavController(R.id.navHostFragment)
		val appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.recipesFragment,
				R.id.favoriteRecipesFragment,
				R.id.foodJokeFragment
			)
		)
		binding.bottomNavigationView.setupWithNavController(navController)
		setupActionBarWithNavController(navController, appBarConfiguration)
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}

}