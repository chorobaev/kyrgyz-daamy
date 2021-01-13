package com.timelysoft.kainarapp.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timelysoft.kainarapp.service.db.entity.BasketEntity
import com.timelysoft.kainarapp.service.db.entity.CategoryEntity
import com.timelysoft.kainarapp.service.db.entity.MenuItemEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(entity: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(entity: List<MenuItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodToBasked(entity: BasketEntity)

    @Query("SELECT * FROM CategoryEntity")
    fun findAllCategories(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM MenuItemEntity Limit 30")
    fun findAllMenuItems(): LiveData<List<MenuItemEntity>>

    @Query("SELECT * FROM BasketEntity WHERE quantity<>0 ORDER BY id")
    fun findAllBasked(): LiveData<List<BasketEntity>>

    @Query("SELECT * FROM MenuItemEntity WHERE MenuItemEntity.id_category=:id")
    fun findByCategory(id: String): LiveData<List<MenuItemEntity>>

    @Query("SELECT * FROM BasketEntity WHERE BasketEntity.id=:id and quantity!=0")
    fun findFoodById(id: Int): BasketEntity?

    @Query("DELETE FROM MenuItemEntity")
    suspend fun deleteAllMenuItems()

    @Query("DELETE FROM CategoryEntity")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM BasketEntity")
    suspend fun deleteBasket()

    @Query("DELETE FROM BasketEntity")
    suspend fun clearBasket()

    @Query("SELECT SUM(price*quantity) FROM BasketEntity WHERE quantity<>0")
    fun amount(): LiveData<Double>
}