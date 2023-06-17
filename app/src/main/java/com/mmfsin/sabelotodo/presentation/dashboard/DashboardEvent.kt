package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data

sealed class DashboardEvent {
    class GetCategory(val result: Category) : DashboardEvent()
    class DashboardData(val data: List<Data>) : DashboardEvent()
    object SomethingWentWrong : DashboardEvent()
}