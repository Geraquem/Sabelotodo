package com.mmfsin.sabelotodo.presentation.categories.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogCuackBinding
import com.mmfsin.sabelotodo.utils.countDown

class CuackDialog : BaseDialog<DialogCuackBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogCuackBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = bottomCustomViewDialog(dialog)

    override fun setUI() {
        binding.apply {
            tvSound.visibility = View.INVISIBLE
            tvSound.text = getBarkText()
            isCancelable = false
            bark()
        }
    }

    private fun getBarkText(): String {
        return when ((1..20).random()) {
            1, 2, 3, 4, 6, 7, 8, 9, 10, 11 -> "Cuack"
            12, 13, 14 -> "Miau"
            15, 16, 17 -> "Guau"
            18, 19, 20 -> "Oink"
            else -> "Pringao" //(5)
        }
    }

    private fun bark() {
        binding.apply {
            countDown(650) {
                tvSound.visibility = View.VISIBLE
                countDown(500) {
                    tvSound.visibility = View.INVISIBLE
                    countDown(400) {
                        dismiss()
                    }
                }
            }
        }
    }
}