package com.mmfsin.sabelotodo.utils

import androidx.fragment.app.FragmentActivity
import com.mmfsin.sabelotodo.base.dialog.ErrorDialog

fun FragmentActivity.showErrorDialog() {
    val dialog = ErrorDialog()
    this.let { dialog.show(it.supportFragmentManager, "") }
}