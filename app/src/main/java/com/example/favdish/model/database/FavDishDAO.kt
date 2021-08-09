package com.example.favdish.model.database

import androidx.room.*
import com.example.favdish.model.entites.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDAO {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("select * from FAV_DISHES_TABLE order by id")
    fun getAllDishesList(): Flow<List<FavDish>>

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Query("select * from FAV_DISHES_TABLE where favorite_dish= 1")
    fun getFavoriteDishesList(): Flow<List<FavDish>>

    @Delete
    suspend fun deleteFavDishDetails(favDish: FavDish)

    @Query("select * from FAV_DISHES_TABLE where type= :filterType")
    fun getFilteredDishesList(filterType: String): Flow<List<FavDish>>
}