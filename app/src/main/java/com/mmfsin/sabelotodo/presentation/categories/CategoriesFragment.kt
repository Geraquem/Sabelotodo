package com.mmfsin.sabelotodo.presentation.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import com.mmfsin.sabelotodo.databinding.ItemCategoryBinding

class CategoriesFragment : Fragment(), CategoriesView {

    private var _bdg: FragmentCategoriesBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { CategoriesPresenter(this) }

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getCategoriesData()
    }

    override fun setCategoriesData(categories: List<CategoryDTO>) {
        with(binding) {
            for (category in categories) {
                when (category.name) {
                    getString(R.string.spanish_age) -> setData(category, cardOne)
                    getString(R.string.global_age) -> setData(category, cardTwo)
                    getString(R.string.cartoon_creations) -> setData(category, cardThree)
                    getString(R.string.films_series) -> setData(category, cardFour)
                    getString(R.string.videogames) -> setData(category, cardFive)
                    getString(R.string.important_dates) -> setData(category, cardSix)
                    getString(R.string.creation_objects) -> setData(category, cardSeven)
                }
            }
        }

    }

    private fun setData(category: CategoryDTO, item: ItemCategoryBinding) {
        item.title.text = category.title
        Glide.with(mContext).load(category.image).into(item.image)
        item.description.text = category.description
    }

    override fun somethingWentWrong() {
        Toast.makeText(requireActivity(), " NOT WORKS", Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}