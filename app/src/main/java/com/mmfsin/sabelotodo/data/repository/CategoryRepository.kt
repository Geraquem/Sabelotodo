package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.utils.CATEGORY_REFERENCE
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ICategoryRepository {

    private val reference = Firebase.database.reference.child(CATEGORY_REFERENCE)

    override fun getCategoriesFromRealm(): List<Category> {
        return realmDatabase.getObjectsFromRealm { where<Category>().findAll() }
    }

    override suspend fun getCategoriesFromFirebase(): List<Category> {
        val latch = CountDownLatch(1)
        val categories = mutableListOf<Category>()
        reference.get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(Category::class.java)?.let { category ->
                    categories.add(category)
                    saveCategory(category)
                }
            }
            latch.countDown()
        }.addOnFailureListener {
            latch.countDown()
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return categories
    }

    private fun saveCategory(category: Category) = realmDatabase.addObject { category }
}