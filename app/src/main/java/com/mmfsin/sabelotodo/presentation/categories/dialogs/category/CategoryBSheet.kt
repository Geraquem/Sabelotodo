package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseBottomSheet
import com.mmfsin.sabelotodo.databinding.DialogCategoryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.getCategoryText
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBSheet(private val id: String, private val listener: ICategoryListener) :
    BaseBottomSheet<DialogCategoryBinding>() {

    private val viewModel: CategoryBSheetViewModel by viewModels()

    private var category: Category? = null

    override fun inflateView(inflater: LayoutInflater) = DialogCategoryBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        viewModel.getCategory(id)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)

                val metrics = Resources.getSystem().displayMetrics
                val maxHeight = (metrics.heightPixels * 0.8).toInt()
                it.layoutParams.height = maxHeight
                behavior.peekHeight = maxHeight
                it.requestLayout()

                it.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_header_dialog)
            }
        }
        return dialog
    }

    override fun setUI() {
        isCancelable = true
        binding.apply {
            category?.let {
                context?.let { c -> Glide.with(c).load(it.image).into(ivImage) }
                tvCategory.text = it.title
                setTexts(it)
                tvGuesserRecord.text = it.guesserRecord.toString()
                tvTemporaryRecord.text = it.temporaryRecord.toString()
                background.setBackgroundColor(Color.parseColor(it.colorEnd))
            }
        }
    }

    private fun setTexts(category: Category) {
        val texts = activity?.applicationContext?.getCategoryText(category.id)
        binding.apply {
            tvGuesser.text = texts?.first
            tvTemporary.text = texts?.second
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
                is CategoryBSheetEvent.GetCategory -> {
                    category = event.category
                    setUI()
                }

                is CategoryBSheetEvent.AvailableMusicMaster -> {}
                is CategoryBSheetEvent.SomethingWentWrong -> error()
                else -> {}
            }
        }
    }

    private fun error() = activity?.showErrorDialog()
}