package com.timelysoft.kainarapp.service.db

import androidx.lifecycle.LiveData
import com.timelysoft.kainarapp.service.db.entity.BasketEntity
import com.timelysoft.kainarapp.service.db.entity.BasketEntity2
import com.timelysoft.kainarapp.service.db.entity.CategoryEntity
import com.timelysoft.kainarapp.service.db.entity.MenuItemEntity
import kotlinx.coroutines.*

class RoomRepository(private val dao: RoomDao) {


    fun insertFoodToBasket(entity: BasketEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertFoodToBasked(entity)
        }
    }
    fun insertMenuItem(entity : List<MenuItemEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertMenuItem(entity)
        }
    }

    fun findAllIMenuItems(): LiveData<List<MenuItemEntity>> {
        return dao.findAllMenuItems()
    }


    fun findAllBaskets(): LiveData<List<BasketEntity>> {
        return dao.findAllBasked()
    }

    fun findFoodBasketByID(id: Int): BasketEntity? {
        return dao.findFoodById(id)
    }

    fun findAllCategories(): LiveData<List<CategoryEntity>> {
        return dao.findAllCategories()
    }

    fun findMenuItemsByCategory(id: String): LiveData<List<MenuItemEntity>> {
        return dao.findByCategory(id)
    }
/*
    suspend fun updateMenu(data: CategoriesResponse?) = withContext(Dispatchers.IO) {
        val group = data?. {
            it.categpath.substringAfterLast("\\")
        }
        dao.deleteAllCategories()
        dao.deleteAllMenuItems()
        dao.clearBasket()

        var index = 0
        val categories = arrayListOf<CategoryEntity>()
        val menuitems = arrayListOf<MenuItemEntity>()
        group.forEach {
            ++index
            categories.add(CategoryEntity(index, it.key, ""))
            val menus = it.value.filter {
                it.rests != 0
            }.map { item->

                MenuItemEntity(
                    null,
                    item.code,
                    item.name,
                    item.price,
                    item.image,
                    item.rests,
                    index,
                    item.recipe
                )


            }
            menuitems.addAll(menus)
        }

        AppPreferences.menuVersion(AppPreferences.globalId, data!!.version)
        dao.insertCategory(categories)
        dao.insertMenuItem(menuitems)
        delay(4000)
    }


    fun amount(): LiveData<Double> {
        return dao.amount()
    }

    fun clearBasket() {
        AppPreferences.amount = 0
        CoroutineScope(Dispatchers.IO).launch {
            dao.clearBasket()
        }
    }

     */



}