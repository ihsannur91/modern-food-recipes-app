package com.example.menumu.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.menumu.data.databases.RecipesEntity
import com.example.menumu.data.Repository
import com.example.menumu.models.FoodRecipe
import com.example.menumu.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
	private val repository: Repository,
	application: Application
): AndroidViewModel(application) {

	/**DATABASE*/

	val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

	private fun insertRecipes(recipesEntity: RecipesEntity) =
		viewModelScope.launch (Dispatchers.IO) {
			repository.local.insertRecipes(recipesEntity)
		}

	/**RETROFIT*/
	var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
	var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

	fun getRecipes(queries: Map<String,String>) = viewModelScope.launch {
		getRecipesSafeCall(queries)
	}

	fun searchRecipes(searchQuery: Map<String,String>) = viewModelScope.launch {
		searchRecipesSafeCall(searchQuery)
	}


	private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
		recipesResponse.value = NetworkResult.Loading()
		if(hasInternetConnection()) {
			try {
				val response = repository.remote.getRecipes(queries)
				recipesResponse.value = handleFoodRecipesResponse(response)

				val foodRecipe = recipesResponse.value!!.data
				if (foodRecipe != null) {
					offlineCacheRecipes(foodRecipe)
				}
			} catch (e: Exception) {
				recipesResponse.value = NetworkResult.Error("Recipes Not Found.")
			}
		} else {
			recipesResponse.value = NetworkResult.Error("No Internet Connection")
		}
	}

	private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
		searchedRecipesResponse.value = NetworkResult.Loading()
		if(hasInternetConnection()) {
			try {
				val response = repository.remote.searchRecipes(searchQuery)
				searchedRecipesResponse.value = handleFoodRecipesResponse(response)
			} catch (e: Exception) {
				searchedRecipesResponse.value = NetworkResult.Error("Recipes Not Found.")
			}
		} else {
			searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection")
		}
	}

	private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
		val recipesEntity = RecipesEntity(foodRecipe)
		insertRecipes(recipesEntity)
	}

	private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
		when {
			response.message().toString().contains("timeout") -> {
				return NetworkResult.Error("Time Out")
			}
			response.code() == 402 -> {
				return NetworkResult.Error("API Key Limited.")
			}
			response.body()!!.results.isNullOrEmpty() -> {
				return NetworkResult.Error("Recipes Note Found.")
			}
			response.isSuccessful -> {
				val foodRecipes = response.body()
				return NetworkResult.Success(foodRecipes!!)
			}
			else -> {
				return NetworkResult.Error(response.message())
			}
		}
	}

	private fun hasInternetConnection(): Boolean{
		val connectivityManager = getApplication<Application>().getSystemService(
			Context.CONNECTIVITY_SERVICE
		) as ConnectivityManager
		val activeNetwork = connectivityManager.activeNetwork?: return false
		val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
		return when {
			capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
			else -> false
		}
	}

}