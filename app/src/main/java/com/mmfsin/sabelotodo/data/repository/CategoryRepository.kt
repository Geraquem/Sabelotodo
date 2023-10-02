package com.mmfsin.sabelotodo.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    @ApplicationContext val context: Context,
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
        getDataFromFirebase(getSavedVersion())
        return getCategoriesFromRealm()
    }

    private suspend fun getDataFromFirebase(savedVersion: Long) {
        val latch = CountDownLatch(1)
        reference.get().addOnSuccessListener {
            val version = it.child(VERSION).value as Long
            if (version == savedVersion) {
                latch.countDown()
            } else {
                saveVersion(newVersion = version)

                val availableMM = it.child(MUSIC_MASTER).value as Boolean
                updateAvailableMM(availableMM)

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


    private fun saveVersion(newVersion: Long) {
        val editor = getSharedPreferences().edit()
        editor.putLong(SAVED_VERSION, newVersion)
        editor.apply()
    }

    private fun getSavedVersion(): Long {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, MODE_PRIVATE)
        return sharedPreferences.getLong(SAVED_VERSION, -1)
    }

    private fun updateAvailableMM(available: Boolean) {
        val editor = getSharedPreferences().edit()
        editor.putBoolean(AVAILABLE_MUSICMASTER, available)
        editor.apply()
    }

    override fun getAvailableMusicMaster(): Boolean {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, MODE_PRIVATE)
        return sharedPreferences.getBoolean(AVAILABLE_MUSICMASTER, false)
    }

    private fun getSharedPreferences() = context.getSharedPreferences(MY_SHARED_PREFS, MODE_PRIVATE)

    private fun saveCategory(category: Category) = realmDatabase.addObject { category }
}