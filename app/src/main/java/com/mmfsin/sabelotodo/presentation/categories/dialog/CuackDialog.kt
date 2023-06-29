package com.mmfsin.sabelotodo.presentation.categories.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogExitBinding

class CuackDialog: BaseDialog<DialogExitBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogExitBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
    }

    override fun setListeners() {
        binding.apply {
            btnStay.setOnClickListener { dismiss() }
            btnExit.setOnClickListener {
                dismiss()
            }
        }
    }
}