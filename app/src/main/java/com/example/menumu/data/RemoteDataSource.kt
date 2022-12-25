package com.example.menumu.data

import com.example.menumu.data.network.FoodRecipesApi
import com.example.menumu.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
	private val foodRecipesApi: FoodRecipesApi
){

	suspend fun getRecipes(queries: Map<String,String>): Response<FoodRecipe> {
		return foodRecipesApi.getRecipes(queries)
	}

	suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe>{
		return foodRecipesApi.searchRecipes(searchQuery)
	}

}