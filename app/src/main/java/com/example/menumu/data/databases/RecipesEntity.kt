package com.example.menumu.data.databases

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.menumu.models.FoodRecipe
import com.example.menumu.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
	var foodRecipe: FoodRecipe
) {
	@PrimaryKey(autoGenerate = false)
	var id: Int = 0
}

