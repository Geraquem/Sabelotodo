package com.mmfsin.sabelotodo.domain.interfaces

import com.mmfsin.sabelotodo.domain.models.Data

interface IDashboardRepository {
    suspend fun getDashboardData(categoryId: String): List<Data>
}