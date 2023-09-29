package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.models.VersionDTO
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.utils.CATEGORIES
import com.mmfsin.sabelotodo.utils.VERSION
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ICategoryRepository {

    private val reference = Firebase.database.reference

    override fun getCategory(id: String): Category? {
        val categories =
            realmDatabase.getObjectsFromRealm { where<Category>().equalTo("id", id).findAll() }
        return if (categories.isEmpty()) null else categories.first()
    }

    override fun getCategoriesFromRealm(): List<Category> {
        return realmDatabase.getObjectsFromRealm { where<Category>().findAll() }
    }

    override suspend fun getCategories(): List<Category> {
        val savedVersion = realmDatabase.getObjectsFromRealm { where<VersionDTO>().findAll() }
        val actualVersion = if (savedVersion.isEmpty()) -1 else savedVersion.first().version
        getDataFromFirebase(actualVersion)

        return realmDatabase.getObjectsFromRealm { where<Category>().findAll() }
    }

    private suspend fun getDataFromFirebase(savedVersion: Long) {
        val latch = CountDownLatch(1)
        reference.get().addOnSuccessListener {
            val version = it.child(VERSION).value as Long
            realmDatabase.addObject { VersionDTO(VERSION, version) }
            if (version == savedVersion) {
                latch.countDown()
            } else {
                val fbCategories = it.child(CATEGORIES)
                for (child in fbCategories.children) {
                    child.getValue(Category::class.java)?.let { deck ->
                        saveCategory(deck)
                    }
                }
                latch.countDown()
            }

        }.addOnFailureListener {
            latch.countDown()
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
    }

    private fun saveCategory(category: Category) = realmDatabase.addObject { category }
}