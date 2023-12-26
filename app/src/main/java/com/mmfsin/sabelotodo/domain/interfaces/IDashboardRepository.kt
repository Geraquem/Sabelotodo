package com.mmfsin.sabelotodo.domain.interfaces

import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.LoserImages

interface IDashboardRepository {
    suspend fun getDashboardData(categoryId: String): List<Data>
    fun updateGuesserRecord(categoryId: String, record: Int)
    fun updateTemporaryRecord(categoryId: String, record: Int)
    fun getLoserImages(): List<LoserImages>
}