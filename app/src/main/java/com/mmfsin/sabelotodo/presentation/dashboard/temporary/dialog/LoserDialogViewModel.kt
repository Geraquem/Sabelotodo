package com.mmfsin.sabelotodo.presentation.dashboard.temporary.dialog

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.GetLoserImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoserDialogViewModel @Inject constructor(
    private val getLoserImagesUseCase: GetLoserImagesUseCase,
) : BaseViewModel<LoserDialogEvent>() {

    fun getLoserImages() {
        executeUseCase(
            { getLoserImagesUseCase.execute() },
            { result -> _event.value = LoserDialogEvent.LoserImage(result) },
            { _event.value = LoserDialogEvent.SomethingWentWrong }
        )
    }
}