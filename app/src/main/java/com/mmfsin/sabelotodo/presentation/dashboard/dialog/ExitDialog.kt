package com.mmfsin.sabelotodo.presentation.dashboard.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogExitBinding

class ExitDialog(val action: () -> Unit) : BaseDialog<DialogExitBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogExitBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
    }

    override fun setListeners() {
        binding.apply {
            btnStay.setOnClickListener { dismiss() }
            btnExit.setOnClickListener {
                action()
                dismiss()
            }
        }
    }
}