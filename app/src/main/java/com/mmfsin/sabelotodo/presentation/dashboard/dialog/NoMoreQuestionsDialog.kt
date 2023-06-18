package com.mmfsin.sabelotodo.presentation.dashboard.dialog

import android.view.LayoutInflater
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogNoMoreBinding

class NoMoreQuestionsDialog : BaseDialog<DialogNoMoreBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogNoMoreBinding.inflate(inflater)

    override fun setListeners() {
        binding.btnAccept.setOnClickListener {
            activity?.onBackPressed()
            dismiss()
        }
    }
}