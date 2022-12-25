package com.example.menumu.data

import com.example.menumu.data.databases.RecipesDao
import com.example.menumu.data.databases.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
	private val recipesDao: RecipesDao
) {

	fun readDatabase(): Flow<List<RecipesEntity>>{
		return recipesDao.readRecipes()
	}

	suspend fun insertRecipes(recipesEntity: RecipesEntity) {
		recipesDao.insertRecipes(recipesEntity)
	}

}