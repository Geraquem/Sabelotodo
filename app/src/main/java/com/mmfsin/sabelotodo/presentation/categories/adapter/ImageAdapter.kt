package com.mmfsin.sabelotodo.presentation.categories.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.databinding.ItemCategoryImageBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener

class ImageAdapter(
    private val imageList: List<Category>,
    private val listener: ICategoryListener
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val category = imageList[position]
        holder.bind(category, listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_image, parent, false)
        )
    }

    override fun getItemCount(): Int = imageList.size

    fun updateTexts(position: Int) {
        val category = imageList[position]
        listener.onCategoryScrolled(category.title, category.examples, category.colorEnd)
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryImageBinding.bind(view)
        val image = binding.image
        val context: Context = binding.root.context
        fun bind(category: Category, listener: ICategoryListener) {
            binding.apply {
                Glide.with(context).load(category.mainImage).into(image)
                item.setOnClickListener { listener.onCategoryClick(category.id) }
            }
        }
    }
}