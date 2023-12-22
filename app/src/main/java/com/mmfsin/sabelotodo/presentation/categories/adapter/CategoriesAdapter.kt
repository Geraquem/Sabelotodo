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
    private val categories: List<Category>, private val listener: ICategoryListener
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(view)
        private val c: Context = binding.root.context
        fun bind(category: Category, listener: ICategoryListener) {
            binding.apply {
                tvTitle.text = category.title
                Glide.with(c).load(category.image).into(image)
                tvDescription.text = category.examples

                setTextButtons(category.id)
                if (category.buttonsOpened) {
                    if (category.id == c.getString(R.string.id_music)) {
                        listener.openMusicMasterDialog(category.id)
                    } else llButtons.visibility = View.VISIBLE
                } else llButtons.visibility = View.GONE

                tvGuesserRecord.text = category.guesserRecord.toString()
                tvTemporaryRecord.text = category.temporaryRecord.toString()

                val startColor = category.colorStart
                val endColor = category.colorEnd
                val colors = intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
                val newGradient =
                    GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors).apply {
                        gradientType = GradientDrawable.LINEAR_GRADIENT
                        cornerRadius = 30f
                        setGradientCenter(0.5f, 0.5f)
                    }
                item.background = newGradient

                btnGuesser.setOnClickListener { listener.startGuesserGame(category.id) }
                btnTemporary.setOnClickListener { listener.startTemporaryGame(category.id) }
            }
        }

        private fun setTextButtons(id: String) {
            binding.apply {
                when (id) {
                    c.getString(R.string.id_spanish_age),
                    c.getString(R.string.id_global_age) -> {
                        btnGuesserText.text = c.getString(R.string.category_dialog_guess_age)
                        btnTemporaryText.text = c.getString(R.string.category_dialog_temporary_age)
                    }

                    c.getString(R.string.id_films_series),
                    c.getString(R.string.id_cartoon_creations),
                    c.getString(R.string.id_videogames) -> {
                        btnGuesserText.text = c.getString(R.string.category_dialog_guess_date)
                        btnTemporaryText.text = c.getString(R.string.category_dialog_temporary_date)
                    }

                    c.getString(R.string.id_important_dates) -> {
                        btnGuesserText.text = c.getString(R.string.category_dialog_guess_date)
                        btnTemporaryText.text =
                            c.getString(R.string.category_dialog_temporary_important_dates)
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, listener)
        holder.itemView.setOnClickListener {
            category.buttonsOpened = !category.buttonsOpened
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = categories.size
}