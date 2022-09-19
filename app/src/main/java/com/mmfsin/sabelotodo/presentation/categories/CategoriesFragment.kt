package com.mmfsin.sabelotodo.presentation.categories

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R.color
import com.mmfsin.sabelotodo.R.string.*
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.data.models.DataToDashDTO
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import com.mmfsin.sabelotodo.databinding.ItemCategoryBinding
import com.mmfsin.sabelotodo.presentation.ICommunication

class CategoriesFragment(private val listener: ICommunication) : Fragment(), CategoriesView {

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
        binding.loading.root.visibility = View.VISIBLE
        presenter.getCategoriesData()
    }

    override fun setCategoriesData(categories: List<CategoryDTO>) {
        with(binding) {
            for (category in categories) {
                when (category.name) {
                    getString(spanish_age) -> setData(
                        category,
                        listener.getRecord(category.name),
                        cardOne,
                        color.g_1,
                        color.g_2
                    )
                    getString(global_age) -> setData(
                        category,
                        listener.getRecord(category.name),
                        cardTwo,
                        color.g_3,
                        color.g_4
                    )
                    getString(films_series) -> setData(
                        category,
                        listener.getRecord(category.name),
                        cardThree,
                        color.g_5,
                        color.g_6
                    )
                    getString(cartoon_creations) -> setData(
                        category,
                        listener.getRecord(category.name),
                        cardFour,
                        color.g_7,
                        color.g_8
                    )
                    getString(videogames) -> setData(
                        category,
                        listener.getRecord(category.name),
                        cardFive,
                        color.g_9,
                        color.g_10
                    )
                    getString(important_dates) -> setData(
                        category,
                        listener.getRecord(category.name),
                        cardSix,
                        color.g_11,
                        color.g_12
                    )

                    getString(music) -> setData(
                        category,
                        0,
                        cardSeven,
                        color.g_7,
                        color.g_8
                    )
                }
            }
        }
        binding.loading.root.visibility = View.GONE
    }

    private fun setData(
        category: CategoryDTO,
        actualRecord: Int,
        item: ItemCategoryBinding,
        color1: Int,
        color2: Int
    ) {
        item.title.text = category.title
        Glide.with(mContext).load(category.image).into(item.image)
        item.description.text = category.description
        item.actualRecord.text = getString(records, actualRecord.toString())

        item.item.background = GradientDrawable().apply {
            colors = intArrayOf(getColor(mContext, color1), getColor(mContext, color2))
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            cornerRadius = 48f
        }

        val data = DataToDashDTO(category.title, category.name, category.image, actualRecord)
        item.item.setOnClickListener {
            if (data.category == getString(music)) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(musicMasterUrl))))
            } else listener.navigateToDashboard(data)
        }
    }

    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}