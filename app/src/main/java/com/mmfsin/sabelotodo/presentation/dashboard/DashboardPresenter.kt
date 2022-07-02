package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.repository.DashBoardRepo
import com.mmfsin.sabelotodo.data.repository.DashBoardRepo.IDashboardRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DashboardPresenter(private val view: DashboardView) : IDashboardRepo, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repo by lazy { DashBoardRepo(this) }

    fun getData() {
        launch(Dispatchers.IO) { repo.getData() }
    }

    override fun setData(data: DataDTO) {
        launch { view.setData(data) }
    }

    override fun somethingWentWrong() {
        launch { view.somethingWentWrong() }
    }

}