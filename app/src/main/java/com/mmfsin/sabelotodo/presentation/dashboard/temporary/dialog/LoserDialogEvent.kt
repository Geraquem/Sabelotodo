package com.mmfsin.sabelotodo.presentation.dashboard.temporary.dialog

sealed class LoserDialogEvent {
    class LoserImage(val image: String) : LoserDialogEvent()
    object SomethingWentWrong : LoserDialogEvent()
}