package com.example.menumu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.menumu.R
import com.example.menumu.adapters.PagerAdapter
import com.example.menumu.databinding.ActivityDetailsBinding
import com.example.menumu.ui.fragments.ingredients.IngredientsFragment
import com.example.menumu.ui.fragments.instructions.InstructionsFragment
import com.example.menumu.ui.fragments.overview.OverviewFragment

class DetailsActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDetailsBinding

	private val args by navArgs<DetailsActivityArgs>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailsBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.toolBar)
		binding.toolBar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val fragments = ArrayList<Fragment>()
		fragments.add(OverviewFragment())
		fragments.add(IngredientsFragment())
		fragments.add(InstructionsFragment())

		val titles = ArrayList<String>()
		titles.add("Overview")
		titles.add("Ingredients")
		titles.add("Instructions")

		val resultBundle = Bundle()
		resultBundle.putParcelable("recipeBundle", args.result)

		val adapter = PagerAdapter(
			resultBundle,
			fragments,
			titles,
			supportFragmentManager
		)

		binding.viewPager.adapter = adapter
		binding.tabLayout.setupWithViewPager(binding.viewPager)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home){
			finish()
		}
		return super.onOptionsItemSelected(item)
	}
}