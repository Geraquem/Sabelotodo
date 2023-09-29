package com.mmfsin.sabelotodo.presentation.categories

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.categories.CategoriesFragmentDirections.Companion.actionCategoriesToDashboard
import com.mmfsin.sabelotodo.presentation.categories.adapter.CategoriesAdapter
import com.mmfsin.sabelotodo.presentation.categories.dialogs.category.CategoryDialog
import com.mmfsin.sabelotodo.presentation.categories.dialogs.category.interfaces.ICategoryDialogListener
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.showErrorDialog
import com.mmfsin.sabelotodo.utils.showFragmentDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>(),
    ICategoryListener, ICategoryDialogListener {

    override val viewModel: CategoriesViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).inDashboard = false

        val checkVersion = (activity as MainActivity).checkVersion
        viewModel.getCategories(checkVersion)
        (activity as MainActivity).checkVersion = false
    }

    override fun setUI() {
        binding.apply {
            setUpToolbar()
            loading.root.isVisible
        }
    }

    private fun setUpToolbar() {
        (activity as MainActivity).apply {
            showBanner(visible = false)
            toolbarIcon(showDuck = true)
            toolbarText(getString(R.string.app_name))
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoriesEvent.Categories -> setCategoryRecycler(event.result)
                is CategoriesEvent.SomethingWentWrong -> activity?.showErrorDialog()
            }
        }
    }

    private fun setCategoryRecycler(categories: List<Category>) {
        if (categories.isNotEmpty()) {
            binding.rvCategory.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter =
                    CategoriesAdapter(categories.sortedBy { it.order }, this@CategoriesFragment)
            }
            binding.loading.root.isVisible = false
        }
    }

    override fun onCategoryClick(id: String) {
        activity?.showFragmentDialog(CategoryDialog.newInstance(id, this@CategoriesFragment))
    }

    override fun startGame(categoryId: String) {
        findNavController().navigate(actionCategoriesToDashboard(categoryId))
    }

    override fun openMusicMaster() {
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.music_master_url))))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}