package io.flaterlab.kyrgyzdaamy.ui

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.flaterlab.kyrgyzdaamy.service.response.Category
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem.Companion.toMenuItem
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse.Companion.toNews
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(private val firestore: FirebaseFirestore,private val storage: FirebaseStorage){

    @ExperimentalCoroutinesApi
    fun getMenu(): Flow<List<Category>?>{
        return callbackFlow {
            val listenerRegistration = firestore.collection("categories").addSnapshotListener { value, error ->
                if (error != null){
                    cancel("Error while fetching menu categories",cause = error)
                    return@addSnapshotListener
                }
                val v = value!!.documents.mapNotNull {
                    it.toObject(Category::class.java)
                }
                offer(v)
            }

            awaitClose {
                listenerRegistration.remove()
            }


        }
    }

    @ExperimentalCoroutinesApi
    fun getMenuItem(categoryId:String):Flow<List<MenuItem>?>{
        return callbackFlow {
            val registration = firestore.collection("categories")
                .document(categoryId).collection("menu_items").addSnapshotListener { value, error ->
                    if (error != null){
                        cancel("Error while fetching menu items", cause = error)
                        return@addSnapshotListener
                    }
                    val v  = value!!.documents.mapNotNull {
                        it.toMenuItem()
                    }
                    offer(v)

                }
            awaitClose {
                registration.remove()
            }
        }

    }

    @ExperimentalCoroutinesApi
    fun getAllMenuItems(): Flow<List<MenuItem>?> {
        return callbackFlow {
            val registration = firestore.collectionGroup("menu_items")
                .addSnapshotListener { value, error ->
                    if (error != null){
                        cancel("Error while fetching items",cause = error)
                        return@addSnapshotListener
                    }

                    val v = value!!.documents.mapNotNull {
                        it.toMenuItem()
                    }
                    offer(v)
                }

            awaitClose{
                registration.remove()
            }
        }
    }

    fun getImagesForMenuItems(categoryId: String):Flow<List<String>> =
        flow {
           val items = storage.reference.child(categoryId).listAll().await().items
            val mutableList = mutableListOf<String>()

            items.forEach {
                mutableList.add(it.downloadUrl.toString())
            }

            emit(mutableList)

        }

    @ExperimentalCoroutinesApi
    fun getNews(): Flow<List<NewsResponse>> =
        callbackFlow {
            val registration= firestore.collection("News").addSnapshotListener { value, error ->
                if (error != null){
                    cancel("Error while fetching news", cause = error)
                    return@addSnapshotListener
                }

                val v= value!!.documents.mapNotNull {
                    it.toNews()
                }
                offer(v)
            }
            awaitClose {
                registration.remove()
            }
        }



}