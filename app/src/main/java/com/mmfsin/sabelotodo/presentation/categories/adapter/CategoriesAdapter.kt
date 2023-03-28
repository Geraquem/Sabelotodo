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
import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import com.mmfsin.sabelotodo.presentation.ICommunication

class CategoriesAdapter(
    private val categories: List<CategoryDTO>,
    private val listener: ICommunication,
    private val onClick: (category: CategoryDTO, record: Int) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(view)
        val context: Context = binding.root.context
        fun bind(
            category: CategoryDTO,
            listener: ICommunication,
            onClick: (category: CategoryDTO, record: Int) -> Unit
        ) {
            binding.apply {
                title.text = category.title
                Glide.with(context).load(category.image).into(image)
                description.text = category.description

                val record = listener.getRecord(category.name)
                actualRecord.text = context.getString(R.string.records, record.toString())

                val startColor = category.colorStart
                val endColor = category.colorEnd
                val colors = intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
                val newGradient =
                    GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors).apply {
                        gradientType = GradientDrawable.LINEAR_GRADIENT
                        cornerRadius = 20f
                        setGradientCenter(0.5f, 0.5f)
                        cornerRadius = 40f
                    }
                item.background = newGradient

                item.setOnClickListener { onClick(category, record) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position], listener, onClick)
    }

    override fun getItemCount(): Int = categories.size
}