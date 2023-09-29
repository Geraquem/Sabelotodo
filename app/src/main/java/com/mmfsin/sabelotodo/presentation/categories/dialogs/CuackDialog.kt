package com.mmfsin.sabelotodo.presentation.categories.dialogs

import android.app.Dialog
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogCuackBinding

class CuackDialog : BaseDialog<DialogCuackBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogCuackBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun setUI() {
        binding.apply {
            tvSound.visibility = View.INVISIBLE
            tvSound.text = getBarkText()
            isCancelable = true
            bark()
        }
    }

    private fun getBarkText(): String {
        return when ((1..20).random()) {
            1, 2, 3, 4, 6, 7, 8, 9, 10 -> "Cuack"
            12, 13, 14 -> "Miau"
            15, 16, 17 -> "Guau"
            18, 19, 20 -> "Oink"
            else -> "Pringao" //(5)
        }
    }

    private fun bark() {
        binding.apply {
            object : CountDownTimer(650, 100) {
                override fun onTick(p0: Long) {}
                override fun onFinish() {
                    tvSound.visibility = View.VISIBLE
                    object : CountDownTimer(500, 100) {
                        override fun onTick(p0: Long) {}
                        override fun onFinish() {
                            tvSound.visibility = View.INVISIBLE
                            object : CountDownTimer(400, 100) {
                                override fun onTick(p0: Long) {}
                                override fun onFinish() {
                                    activity?.let { dismiss() }
                                }
                            }.start()
                        }
                    }.start()
                }
            }.start()
        }
    }
}