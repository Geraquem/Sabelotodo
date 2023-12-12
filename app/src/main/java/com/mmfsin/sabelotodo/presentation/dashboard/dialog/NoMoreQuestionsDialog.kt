package com.mmfsin.sabelotodo.presentation.dashboard.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogNoMoreBinding

class NoMoreQuestionsDialog : BaseDialog<DialogNoMoreBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogNoMoreBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setListeners() {
        binding.btnAccept.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
            dismiss()
        }
    }
}