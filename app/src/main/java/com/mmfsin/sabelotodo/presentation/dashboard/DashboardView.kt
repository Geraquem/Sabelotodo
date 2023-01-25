package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.models.ResultType

interface DashboardView {
    fun setDataList(list: List<String>)
    fun setQuestionData(data: DataDTO)
    fun handleDescription(enable: Boolean, description: String)
    fun setTwoLongitudePinView()
    fun showSolution(solution: String, type: ResultType)
    fun somethingWentWrong()
}