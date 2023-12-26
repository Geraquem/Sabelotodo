package com.mmfsin.sabelotodo.presentation.dashboard.temporary.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogTemporaryLoserBinding
import com.mmfsin.sabelotodo.presentation.dashboard.temporary.interfaces.ITemporaryListener
import com.mmfsin.sabelotodo.utils.animateDialog
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoserDialog(private val listener: ITemporaryListener) :
    BaseDialog<DialogTemporaryLoserBinding>() {

    private val viewModel: LoserDialogViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater) =
        DialogTemporaryLoserBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun onResume() {
        super.onResume()
        requireDialog().animateDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        viewModel.getLoserImages()
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

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is LoserDialogEvent.LoserImage -> setLoserImage(event.image)
                is LoserDialogEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setLoserImage(image: String) {
        binding.apply {
            if (image.isBlank()) ivImage.setImageResource(R.drawable.ic_sad)
            else activity?.let { Glide.with(it.applicationContext).load(image).into(ivImage) }
        }
    }

    private fun error() = activity?.showErrorDialog()

    companion object {
        fun newInstance(listener: ITemporaryListener): LoserDialog {
            return LoserDialog(listener)
        }
    }
}