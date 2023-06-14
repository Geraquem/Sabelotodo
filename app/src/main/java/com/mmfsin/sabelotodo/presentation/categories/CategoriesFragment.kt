package com.mmfsin.sabelotodo.presentation.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>() {

    override val viewModel: CategoriesViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategories()
    }

    override fun setUI() {
        binding.apply {
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoriesEvent.Categories -> {
                    val a = 2
                }

                is CategoriesEvent.SomethingWentWrong -> {

                }
            }
        }
    }

//
//    override fun setCategoriesData(categories: List<CategoryDTO>) {
//        binding.rvCategory.apply {
//            layoutManager = LinearLayoutManager(mContext)
////            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
//            adapter = CategoriesAdapter(categories, listener) { category, record ->
//                onCategoryClick(category, record)
//            }
//        }
//        binding.loading.root.visibility = View.GONE
//    }
//
//    private fun onCategoryClick(category: CategoryDTO, actualRecord: Int) {
//        val data = DataToDashDTO(category.title, category.name, category.image, actualRecord)
//        if (data.category == getString(music)) {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(musicMasterUrl))))
//        } else listener.navigateToDashboard(data)
//    }
//
//    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}