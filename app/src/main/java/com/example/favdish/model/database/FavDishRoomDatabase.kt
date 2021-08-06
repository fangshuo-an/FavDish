package com.example.favdish.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favdish.model.entites.FavDish

@Database(entities = [FavDish::class], version = 1)
abstract class FavDishRoomDatabase : RoomDatabase() {

    abstract fun favDishDao():FavDishDAO

    companion object {
        @Volatile
        private var INSTANCE: FavDishRoomDatabase? = null

        fun getDatabase(context: Context): FavDishRoomDatabase {
            //if the instance is not null,then return it
            //if it is , then create the database
            return INSTANCE ?: synchronized(this) {
                /*   val instance = Room.databaseBuilder(
                       context.applicationContext,
                       FavDishRoomDatabase::class.java,
                       "fav_dish_database"
                   ).fallbackToDestructiveMigration()
                       .build()
                   INSTANCE = instance
                   instance*/
                Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "fav_dish_database"
                ).fallbackToDestructiveMigration()
                    .build().apply {
                        INSTANCE = this
                    }
            }
        }
    }
}