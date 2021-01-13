package com.timelysoft.kainarapp.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.timelysoft.kainarapp.service.db.entity.BasketEntity
import com.timelysoft.kainarapp.service.db.entity.CategoryEntity
import com.timelysoft.kainarapp.service.db.entity.MenuItemEntity


@Database(
    entities = [
        CategoryEntity::class,
        BasketEntity::class,
        MenuItemEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDao(): RoomDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "KainarApp"
                ).build()
                this.instance = instance
                instance
            }

        }
    }
}