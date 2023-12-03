package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.BOTTOM_TOP
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogCategoryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.animateDialog
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicDialog(private val id: String, private val listener: ICategoryListener) :
    BaseDialog<DialogCategoryBinding>() {

    private val viewModel: CategoryDialogViewModel by viewModels()

    private var category: Category? = null

    override fun inflateView(inflater: LayoutInflater) = DialogCategoryBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        viewModel.getCategory(id)
    }

    override fun setUI() {
        isCancelable = true
        binding.apply {
            category?.let {
                checkIfMusic(it.id)
                setBackground(it.colorStart, it.colorEnd)
                tvTitle.text = it.title
                tvDescription.text = it.description
                tvGuesserRecord.text = it.guesserRecord.toString()
                tvTemporaryRecord.text = it.temporaryRecord.toString()
            }
        }
    }

    private fun setBackground(colorStart: String, colorEnd: String) {
        binding.apply {
//            val colors = intArrayOf(Color.parseColor(colorStart), Color.parseColor(colorEnd))
//            val newGradient =
//                GradientDrawable(BOTTOM_TOP, colors).apply {
//                    gradientType = GradientDrawable.LINEAR_GRADIENT
//                    cornerRadius = 30f
//                    setGradientCenter(0.5f, 0.5f)
//                }
//            vBg.background = newGradient
//            btnGuesser.background = newGradient
//            btnTemporary.background = newGradient
        }
    }

    private fun checkIfMusic(id: String) {
        if (id == getString(R.string.category_music)) {
            binding.apply {
//                duckImage.visibility = View.VISIBLE
//                tvTitleExamples.visibility = View.GONE
                llHighScore.visibility = View.GONE
                btnGuesserText.text = getString(R.string.category_dialog_download)
//                btnGuesserImage.setImageResource(R.drawable.ic_download)
                viewModel.checkIfAvailable()
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnGuesser.setOnClickListener {
                category?.let {
                    if (id == getString(R.string.category_music)) listener.openMusicMaster()
                    else {
                        listener.startGuesserGame(it.id)
                        dismiss()
                    }
                }
            }

            btnTemporary.setOnClickListener {
                category?.let {
                    if (id == getString(R.string.category_music)) listener.openMusicMaster()
                    else {
                        listener.startCTemporaryGame(it.id)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoryDialogEvent.GetCategory -> {
                    category = event.category
                    setUI()
                }

                is CategoryDialogEvent.AvailableMusicMaster -> availableMusicMaster(event.available)
                is CategoryDialogEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun availableMusicMaster(available: Boolean) {
        binding.apply {
            btnTemporary.isVisible = false
            btnGuesser.isEnabled = available
            if (available) {
                btnGuesserText.text = getString(R.string.category_dialog_download)
//                btnGuesserImage.setImageResource(R.drawable.ic_download)
            } else {
                btnGuesserText.text = getString(R.string.category_dialog_soon)
//                btnGuesserImage.visibility = View.GONE
            }
        }
    }

    private fun error() = activity?.showErrorDialog()
}