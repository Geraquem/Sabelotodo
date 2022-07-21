package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.data.models.DataDTO

interface DashboardView {
    fun setDataList(list: List<String>)
    fun setQuestionData(data: DataDTO)
    fun handleDescription(enable: Boolean, description: String)
    fun somethingWentWrong()
}