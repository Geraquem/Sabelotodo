package com.mmfsin.sabelotodo.presentation.dashboard.temporary.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogTemporaryLooserBinding
import com.mmfsin.sabelotodo.presentation.dashboard.temporary.interfaces.ITemporaryListener
import com.mmfsin.sabelotodo.utils.animateDialog

class LooserDialog(private val listener: ITemporaryListener) :
    BaseDialog<DialogTemporaryLooserBinding>() {

    override fun inflateView(inflater: LayoutInflater) =
        DialogTemporaryLooserBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun onResume() {
        super.onResume()
        requireDialog().animateDialog()
    }

    override fun setUI() {
        isCancelable = false
    }

    override fun setListeners() {
        binding.apply {
            btnRematch.setOnClickListener {
                listener.rematch()
                dismiss()
            }
            btnExit.setOnClickListener {
                listener.exitGame()
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(listener: ITemporaryListener): LooserDialog {
            return LooserDialog(listener)
        }
    }
}