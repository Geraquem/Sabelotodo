package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.domain.models.DataDTO
import com.mmfsin.sabelotodo.domain.models.ResultType

interface DashboardView {
    fun dataListFilled(list: List<DataDTO>)

    fun handleDescription(enable: Boolean, description: String)
    fun setTwoLongitudePinView()
    fun showSolution(solution: String, type: ResultType)
    fun somethingWentWrong()
}