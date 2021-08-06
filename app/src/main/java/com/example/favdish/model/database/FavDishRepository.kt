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
}