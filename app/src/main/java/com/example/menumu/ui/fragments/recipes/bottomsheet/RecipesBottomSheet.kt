package com.example.menumu.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.menumu.R
import com.example.menumu.databinding.RecipesBottomSheetBinding
import com.example.menumu.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.menumu.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.menumu.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

	private var _binding: RecipesBottomSheetBinding? = null
	private val binding get() = _binding!!
	private lateinit var mView: View

	private lateinit var recipesViewModel: RecipesViewModel

	private var mealTypeChip = DEFAULT_MEAL_TYPE
	private var mealTypeChipId = 0
	private var dietTypeChip = DEFAULT_DIET_TYPE
	private var dietTypeChipId = 0


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = RecipesBottomSheetBinding.inflate(inflater, container, false)
		mView = binding.root

		recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
			mealTypeChip = value.selectedMealType
			dietTypeChip = value.selectedDietType

			updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
			updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)

		}

		binding.mealTypeChipGroup.setOnCheckedStateChangeListener{ group, selectedChipId ->
			val chip = group.findViewById<Chip>(selectedChipId)
			val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
			mealTypeChip = selectedMealType
			mealTypeChipId = selectedChipId
		}

		binding.applyBtn.setOnClickListener {
			recipesViewModel.saveMealAndDietType(
				mealTypeChip,
				mealTypeChipId,
				dietTypeChip,
				dietTypeChipId
			)
			val action =
				RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
			findNavController().navigate(action)
		}

		return mView
	}

	private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
		if (chipId != 0) {
			try {
				chipGroup.findViewById<Chip>(chipId).isChecked = true
			} catch (e: Exception) {
				Log.d("RecipesBottomSheet", e.message.toString())
			}
		}
	}

}