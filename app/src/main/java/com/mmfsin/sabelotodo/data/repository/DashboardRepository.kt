package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.mappers.toDataList
import com.mmfsin.sabelotodo.data.mappers.toLoserImagesList
import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.models.LoserImagesDTO
import com.mmfsin.sabelotodo.data.models.UserRecordDTO
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.LoserImages
import com.mmfsin.sabelotodo.utils.QUESTIONS
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


class DashboardRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : IDashboardRepository {

    private val reference = Firebase.database.reference.child(QUESTIONS)

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

    override fun updateGuesserRecord(categoryId: String, record: Int) {
        val userRecords = realmDatabase.getObjectsFromRealm {
            where<UserRecordDTO>().equalTo("id", categoryId).findAll()
        }
        /** Si empty significa que es la primera vez que guardo */
        if (userRecords.isEmpty()) {
            val userRecord = UserRecordDTO(categoryId, record, 0)
            realmDatabase.addObject { userRecord }

        } else {
            val userRecord = userRecords.first()
            userRecord.guesserRecord = record
            realmDatabase.addObject { userRecord }
        }
    }

    override fun updateTemporaryRecord(categoryId: String, record: Int) {
        val userRecords = realmDatabase.getObjectsFromRealm {
            where<UserRecordDTO>().equalTo("id", categoryId).findAll()
        }
        /** Si empty significa que es la primera vez que guardo */
        if (userRecords.isEmpty()) {
            val userRecord = UserRecordDTO(categoryId, 0, record)
            realmDatabase.addObject { userRecord }

        } else {
            val userRecord = userRecords.first()
            userRecord.temporaryRecord = record
            realmDatabase.addObject { userRecord }
        }
    }

    override fun getLoserImages(): List<LoserImages> {
        val loserImages = realmDatabase.getObjectsFromRealm {
            where<LoserImagesDTO>().findAll()
        }
        return loserImages.toLoserImagesList()
    }
}
