package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import com.mmfsin.sabelotodo.utils.CATEGORY_REFERENCE
import io.realm.kotlin.where
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ICategoryRepository {

    private val reference = Firebase.database.reference.child(CATEGORY_REFERENCE)

    override fun getCategoriesData(): List<CategoryDTO> {
        return realmDatabase.getObjectsFromRealm { where<CategoryDTO>().findAll() }
    }

//    fun getCategories() {
//        val categories = realmDatabase.getObjectsFromRealm { where<CategoryDTO>().findAll() }
//        if (categories.isEmpty()) getCategoriesDataFromFirebase()
//        else listener.setCategoriesData(categories.sortedBy { it.order })
//    }
//
//    private fun getCategoriesDataFromFirebase() {
//        reference.get().addOnSuccessListener {
//            for (child in it.children) {
//                child.getValue(CategoryDTO::class.java)?.let { category ->
//                    saveCategories(category)
//                }
//            }
//            getCategories()
//
//        }.addOnFailureListener {
//            listener.somethingWentWrong()
//        }
//    }
//
//    private fun saveCategories(category: CategoryDTO) = realm.addObject { category }
}