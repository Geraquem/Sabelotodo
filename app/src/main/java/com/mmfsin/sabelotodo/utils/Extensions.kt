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

fun loadingCountDown(action: () -> Unit) {
    object : CountDownTimer(100, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            action()
        }
    }.start()
}

fun countDown(duration: Long, action: () -> Unit) {
    object : CountDownTimer(duration, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            action()
        }
    }.start()
}

fun View.animateY(pos: Float, duration: Long) =
    this.animate().translationY(pos).setDuration(duration)

fun View.animateX(pos: Float, duration: Long) =
    this.animate().translationX(pos).setDuration(duration)

fun <T1 : Any, T2 : Any, R : Any> checkNotNulls(p1: T1?, p2: T2?, block: (T1, T2) -> R): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}