package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogCategoryBinding
import com.mmfsin.sabelotodo.databinding.DialogCategoryMusicBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.animateDialog
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicDialog(private val id: String, private val listener: ICategoryListener) :
    BaseDialog<DialogCategoryMusicBinding>() {

    private val viewModel: CategoryDialogViewModel by viewModels()

    private var category: Category? = null

    override fun inflateView(inflater: LayoutInflater) =
        DialogCategoryMusicBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun onResume() {
        super.onResume()
        requireDialog().animateDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        viewModel.getCategory(id)
    }

    override fun setUI() {
        isCancelable = true
        binding.apply {
            category?.let {
                tvTitle.text = getString(R.string.category_dialog_music_master)
                tvDescription.text = it.description
                context?.let { c ->
                    Glide.with(c).load(it.image).into(ivMusic)
                }
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnDownload.setOnClickListener { listener.openMusicMaster() }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoryDialogEvent.GetCategory -> {
                    category = event.category
                    setUI()
                    viewModel.checkIfAvailable()
                }

                is CategoryDialogEvent.AvailableMusicMaster -> {
                    availableMusicMaster(event.available)
                }

                is CategoryDialogEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun availableMusicMaster(available: Boolean) {
        binding.apply {
            btnDownload.isEnabled = available
            ivDownload.isVisible = available

            val text = if (available) R.string.category_dialog_download
            else R.string.category_dialog_soon
            btnDownloadText.text = getString(text)
        }
    }

    private fun error() = activity?.showErrorDialog()

    companion object {
        fun newInstance(id: String, listener: ICategoryListener): MusicDialog {
            return MusicDialog(id, listener)
        }
    }
}