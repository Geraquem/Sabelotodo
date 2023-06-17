package com.mmfsin.sabelotodo.base.dialog

import android.view.LayoutInflater
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogErrorBinding

class ErrorDialog : BaseDialog<DialogErrorBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogErrorBinding.inflate(inflater)

    override fun setListeners() {
        binding.btnAccept.setOnClickListener { activity?.onBackPressed() }
    }
}