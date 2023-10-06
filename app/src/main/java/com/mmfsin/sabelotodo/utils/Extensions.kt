package com.mmfsin.sabelotodo.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.mmfsin.sabelotodo.base.dialog.ErrorDialog

fun FragmentActivity.showErrorDialog() = this.showFragmentDialog(ErrorDialog())

fun FragmentActivity?.showFragmentDialog(dialog: DialogFragment) =
    this?.let { dialog.show(it.supportFragmentManager, "") }

fun Dialog.animateDialog() {
    val dialogView = this.window?.decorView
    dialogView?.let {
        it.scaleX = 0f
        it.scaleY = 0f
        val scaleXAnimator = ObjectAnimator.ofFloat(it, View.SCALE_X, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(it, View.SCALE_Y, 1f)
        AnimatorSet().apply {
            playTogether(scaleXAnimator, scaleYAnimator)
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
}

fun countDown(action: () -> Unit) {
    object : CountDownTimer(2800, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            action()
        }
    }.start()
}