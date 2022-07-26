package com.mmfsin.sabelotodo.presentation.categories

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.R.string.*
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.data.models.DataToDash
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import com.mmfsin.sabelotodo.databinding.ItemCategoryBinding
import com.mmfsin.sabelotodo.presentation.ICommunication

class CategoriesFragment(val listener: ICommunication) : Fragment(), CategoriesView {

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
        /** Loading VISIBLE */
        presenter.getCategoriesData()
    }

    override fun setCategoriesData(categories: List<CategoryDTO>) {
        with(binding) {
            for (category in categories) {
                when (category.name) {
                    getString(spanish_age) -> setData(category, cardOne, R.color.g_1, R.color.g_2)
                    getString(global_age) -> setData(category, cardTwo, R.color.g_3, R.color.g_4)
                    getString(films_series) -> setData(
                        category,
                        cardThree,
                        R.color.g_5,
                        R.color.g_6
                    )
                    getString(cartoon_creations) -> setData(category, cardFour, R.color.g_7, R.color.g_8)
                    getString(videogames) -> setData(category, cardFive, R.color.g_9, R.color.g_10)
                    getString(important_dates) -> setData(
                        category,
                        cardSix,
                        R.color.g_11,
                        R.color.g_12
                    )
                    getString(creation_objects) -> setData(
                        category,
                        cardSeven,
                        R.color.g_5,
                        R.color.g_6
                    )
                }
            }
        }
        /** Loading GONE */
    }

    private fun setData(
        category: CategoryDTO,
        item: ItemCategoryBinding,
        color1: Int,
        color2: Int
    ) {
        item.title.text = category.title
        Glide.with(mContext).load(category.image).into(item.image)
        item.description.text = category.description

        item.item.background = GradientDrawable().apply {
            colors = intArrayOf(getColor(mContext, color1), getColor(mContext, color2))
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            cornerRadius = 48f
        }

        val dataToDash = DataToDash(category.name, category.image)
        item.item.setOnClickListener { listener.navigateToDashboard(dataToDash) }
    }

    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}