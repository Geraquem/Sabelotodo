package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.repository.DashboardRepo
import com.mmfsin.sabelotodo.data.repository.DashboardRepo.IDashboardRepo

class DashboardPresenter(private val view: DashboardView) : IDashboardRepo {


    private val repo by lazy { DashboardRepo(this) }

    fun getData() {
        repo.getData()
    }

    override fun setData(data: DataDTO) {
        view.setData(data)
    }

    override fun somethingWentWrong() {
        view.somethingWentWrong()
    }

}