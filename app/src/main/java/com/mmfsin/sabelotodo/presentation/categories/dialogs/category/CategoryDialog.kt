package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseDialog
import com.mmfsin.sabelotodo.databinding.DialogCategoryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDialog(private val id: String, private val listener: ICategoryListener) :
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
                context?.let { c -> Glide.with(c).load(it.image).into(ivTitle) }
                tvTitle.text = it.title
                setTexts(it)
                tvGuesserRecord.text = it.guesserRecord.toString()
                tvTemporaryRecord.text = it.temporaryRecord.toString()
            }
        }
    }

    private fun setTexts(category: Category) {
        binding.apply {
            when (category.id) {
                getString(R.string.id_spanish_age),
                getString(R.string.id_global_age) -> {
                    btnGuesserText.text = getString(R.string.category_dialog_guess_age)
                    btnTemporaryText.text = getString(R.string.category_dialog_temporary_age)
                }

                getString(R.string.id_films_series),
                getString(R.string.id_cartoon_creations),
                getString(R.string.id_videogames) -> {
                    btnGuesserText.text = getString(R.string.category_dialog_guess_date)
                    btnTemporaryText.text = getString(R.string.category_dialog_temporary_date)
                }

                getString(R.string.id_important_dates) -> {
                    btnGuesserText.text = getString(R.string.category_dialog_guess_date)
                    btnTemporaryText.text =
                        getString(R.string.category_dialog_temporary_important_dates)
                }

                else -> {}
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnGuesser.setOnClickListener {
                category?.let {
                    listener.startGuesserGame(it.id)
                    dismiss()
                }
            }

            btnTemporary.setOnClickListener {
                category?.let {
                    listener.startTemporaryGame(it.id)
                    dismiss()
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

                is CategoryDialogEvent.AvailableMusicMaster -> {}
                is CategoryDialogEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun error() = activity?.showErrorDialog()
}