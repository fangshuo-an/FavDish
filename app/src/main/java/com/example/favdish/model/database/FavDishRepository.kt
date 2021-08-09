package com.example.favdish.model.database

import androidx.annotation.WorkerThread
import com.example.favdish.model.entites.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDAO: FavDishDAO) {
    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDAO.insertFavDishDetails(favDish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDAO.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish) {
        favDishDAO.updateFavDishDetails(favDish)
    }

    val favoriteDishes: Flow<List<FavDish>> = favDishDAO.getFavoriteDishesList()

    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish) {
        favDishDAO.deleteFavDishDetails(favDish)
    }

    fun filteredListDishes(value: String): Flow<List<FavDish>> =
        favDishDAO.getFilteredDishesList(value)
}