package com.example.favdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.favdish.model.entites.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDAO {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("select * from FAV_DISHES_TABLE order by id")
    fun getAllDishesList():Flow<List<FavDish>>

}