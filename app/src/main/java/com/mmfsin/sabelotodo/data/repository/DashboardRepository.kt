package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.mappers.toDataList
import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.utils.QUESTIONS_REFERENCE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


class DashboardRepository @Inject constructor() : IDashboardRepository {

    private val reference = Firebase.database.reference.child(QUESTIONS_REFERENCE)

    override suspend fun getDashboardData(categoryId: String): List<Data> {
        val latch = CountDownLatch(1)
        val dataList = mutableListOf<DataDTO>()
        reference.child(categoryId).get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(DataDTO::class.java)?.let { data -> dataList.add(data) }
            }
            latch.countDown()

        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return dataList.toDataList()
    }
}
