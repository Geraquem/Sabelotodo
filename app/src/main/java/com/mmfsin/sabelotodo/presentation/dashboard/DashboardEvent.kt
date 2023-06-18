package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.ResultType

sealed class DashboardEvent {
    class GetCategory(val result: Category) : DashboardEvent()
    class DashboardData(val data: List<Data>) : DashboardEvent()
    class Solution(val solution: ResultType) : DashboardEvent()
    class Record(val result: Boolean) : DashboardEvent()
    object SomethingWentWrong : DashboardEvent()
}