package com.mmfsin.sabelotodo.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.mappers.createLoserImageDTO
import com.mmfsin.sabelotodo.data.mappers.toCategory
import com.mmfsin.sabelotodo.data.mappers.toUserRecord
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.data.models.LoserImagesDTO
import com.mmfsin.sabelotodo.data.models.UserRecordDTO
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.UserRecord
import com.mmfsin.sabelotodo.utils.AVAILABLE_MUSICMASTER
import com.mmfsin.sabelotodo.utils.CATEGORIES
import com.mmfsin.sabelotodo.utils.LOSER_IMAGES
import com.mmfsin.sabelotodo.utils.MUSIC_MASTER
import com.mmfsin.sabelotodo.utils.MY_SHARED_PREFS
import com.mmfsin.sabelotodo.utils.SAVED_VERSION
import com.mmfsin.sabelotodo.utils.VERSION
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject
import androidx.core.content.edit

class CategoryRepository @Inject constructor(
    @ApplicationContext val context: Context, private val realmDatabase: IRealmDatabase
) : ICategoryRepository {

    private val reference = Firebase.database.reference

    override fun getCategory(id: String): Category? {
        val categories = realmDatabase.getObjectsFromRealm {
            query<CategoryDTO>("id == $0", id).find()
        }
        val records = getRecordsFromCategoryId(id)
        return if (categories.isEmpty()) null else categories.first()
            .toCategory(records.guesserRecord, records.temporaryRecord)
    }

    override fun getCategoriesFromRealm(): List<Category> {
        val totalCategories = realmDatabase.getObjectsFromRealm { query<CategoryDTO>().find() }
        val result = mutableListOf<Category>()
        totalCategories.forEach { category ->
            val aux = getCategory(category.id)
            aux?.let { result.add(it) }
        }
        return result
    }

    override suspend fun getCategories(): List<Category> {
        getDataFromFirebase(getSavedVersion())
        return getCategoriesFromRealm()
    }

    private suspend fun getDataFromFirebase(savedVersion: Long) {
        val latch = CountDownLatch(1)
        reference.get().addOnSuccessListener {
            val version = it.child(VERSION).value as Long
            if (version != savedVersion) {
                latch.countDown()
            } else {
                saveVersion(newVersion = version)

                val availableMM = it.child(MUSIC_MASTER).value as Boolean
                updateAvailableMM(availableMM)

                val loserImages = it.child(LOSER_IMAGES)
                for (image in loserImages.children) {
                    val loserImage = createLoserImageDTO(
                        newId = image.key.toString(),
                        newImage = image.value.toString()
                    )
                    saveLoserImages(loserImage)
                }

                val fbCategories = it.child(CATEGORIES)
                for (child in fbCategories.children) {
                    child.getValue(CategoryDTO::class.java)?.let { deck ->
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
        getSharedPreferences().edit {
            putLong(SAVED_VERSION, newVersion)
        }
    }

    private fun getSavedVersion(): Long = getSharedPreferences().getLong(SAVED_VERSION, -1)

    private fun updateAvailableMM(available: Boolean) {
        getSharedPreferences().edit {
            putBoolean(AVAILABLE_MUSICMASTER, available)
        }
    }

    override fun getAvailableMusicMaster(): Boolean {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, MODE_PRIVATE)
        return sharedPreferences.getBoolean(AVAILABLE_MUSICMASTER, false)
    }

    private fun getSharedPreferences() = context.getSharedPreferences(MY_SHARED_PREFS, MODE_PRIVATE)

    private fun saveCategory(category: CategoryDTO) = realmDatabase.addObject { category }

    private fun saveLoserImages(image: LoserImagesDTO) {
        realmDatabase.addObject { image }
    }

    private fun getRecordsFromCategoryId(categoryId: String): UserRecord {
        val records = realmDatabase.getObjectsFromRealm {
            query<UserRecordDTO>("id == $0", categoryId).find()
        }
        return if (records.isEmpty()) return UserRecord(0, 0)
        else records.first().toUserRecord()
    }
}