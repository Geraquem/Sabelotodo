package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogCategoryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
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
                tvTitle.text = it.title
                tvGuesserRecord.text = it.guesserRecord.toString()
                tvTemporaryRecord.text = it.temporaryRecord.toString()
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnGuesser.setOnClickListener { listener.openMusicMaster() }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoryDialogEvent.GetCategory -> {
                    category = event.category
                    setUI()
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