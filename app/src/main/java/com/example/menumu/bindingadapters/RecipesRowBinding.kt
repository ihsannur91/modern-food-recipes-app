package com.example.menumu.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.menumu.R
import com.example.menumu.ui.fragments.recipes.RecipesFragmentDirections
import com.example.menumu.models.Result

class RecipesRowBinding {

	companion object{

		@BindingAdapter("onRecipeClickListener")
		@JvmStatic
		fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: Result){
			try {
				val action =
					RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
				recipeRowLayout.findNavController().navigate(action)
			}catch (e: Exception){
				Log.d("onRecipeClickListener", e.toString())
			}
		}

		@BindingAdapter("loadImageFromUrl")
		@JvmStatic
		fun loadImageFromUrl(imageView: ImageView, imageUrl: String){
			imageView.load(imageUrl){
				crossfade(600)
			}
		}

		//bikin fungsi untukmengkonversi int ke string
		@BindingAdapter("setNumberOfLikes")
		@JvmStatic
		fun setNumberofLikes(textView: TextView,likes: Int){
			textView.text = likes.toString()
		}

		@BindingAdapter("setNumberOfMinutes")
		@JvmStatic
		fun setNumberOfMinutes(textView: TextView, minutes:Int){
			textView.text = minutes.toString()
		}

		@BindingAdapter("applyVeganColor")
		@JvmStatic
		fun applyVeganColor(view: View, vegan: Boolean){
			if (vegan){
				when(view){
					is TextView -> {
						view.setTextColor(
							ContextCompat.getColor(
								view.context,
								R.color.green
							)
						)
					}
					is ImageView -> {
						view.setColorFilter(
							ContextCompat.getColor(
								view.context,
								R.color.green
							)
						)
					}
				}
			}
		}
	}
}