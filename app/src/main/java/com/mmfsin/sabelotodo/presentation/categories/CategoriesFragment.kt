package com.mmfsin.sabelotodo.presentation.categories

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R.color
import com.mmfsin.sabelotodo.R.string.*
import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import com.mmfsin.sabelotodo.domain.models.DataToDashDTO
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import com.mmfsin.sabelotodo.databinding.ItemCategoryBinding
import com.mmfsin.sabelotodo.presentation.ICommunication
import com.mmfsin.sabelotodo.presentation.categories.adapter.CategoriesAdapter

class CategoriesFragment(private val listener: ICommunication) : Fragment(), CategoriesView {

    private var _bdg: FragmentCategoriesBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { CategoriesPresenter(this) }

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.root.visibility = View.VISIBLE
        presenter.getCategoriesData()
    }

    override fun setCategoriesData(categories: List<CategoryDTO>) {
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = CategoriesAdapter(categories, listener) { category, record ->
                onCategoryClick(category, record)
            }
        }
        binding.loading.root.visibility = View.GONE
    }

    private fun onCategoryClick(category: CategoryDTO, actualRecord: Int) {
        val data = DataToDashDTO(category.title, category.name, category.image, actualRecord)
        if (data.category == getString(music)) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(musicMasterUrl))))
        } else listener.navigateToDashboard(data)
    }

    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}