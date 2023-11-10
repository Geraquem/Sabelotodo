package com.mmfsin.sabelotodo.presentation.categories.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.databinding.ItemCategoryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener

class CategoriesAdapter(
    private val categories: List<Category>,
    private val listener: ICategoryListener
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(view)
        val context: Context = binding.root.context
        fun bind(category: Category, listener: ICategoryListener) {
            binding.apply {
                tvTitle.text = category.title
                Glide.with(context).load(category.image).into(image)
                tvDescription.text = category.shortDescription

                val startColor = category.colorStart
                val endColor = category.colorEnd
                val colors = intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
                val newGradient =
                    GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors).apply {
                        gradientType = GradientDrawable.LINEAR_GRADIENT
                        cornerRadius = 30f
                        setGradientCenter(0.5f, 0.5f)
                    }
                item.background = newGradient

                item.setOnClickListener { listener.onCategoryClick(category.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position], listener)
    }

    override fun getItemCount(): Int = categories.size
}