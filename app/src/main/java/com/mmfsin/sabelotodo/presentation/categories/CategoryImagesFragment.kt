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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesImagesBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.categories.CategoryImagesFragmentDirections.Companion.actionCategoriesToGuesser
import com.mmfsin.sabelotodo.presentation.categories.CategoryImagesFragmentDirections.Companion.actionCategoriesToTemporary
import com.mmfsin.sabelotodo.presentation.categories.adapter.ImageAdapter
import com.mmfsin.sabelotodo.presentation.categories.dialogs.category.CategoryDialog
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.animateX
import com.mmfsin.sabelotodo.utils.animateY
import com.mmfsin.sabelotodo.utils.countDown
import com.mmfsin.sabelotodo.utils.showErrorDialog
import com.mmfsin.sabelotodo.utils.showFragmentDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class CategoryImagesFragment : BaseFragment<FragmentCategoriesImagesBinding, CategoriesViewModel>(),
    ICategoryListener {

    override val viewModel: CategoriesViewModel by viewModels()

    private var adapter: ImageAdapter? = null
    private lateinit var mContext: Context

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCategoriesImagesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).inDashboard = false

        val fromRealm = (activity as MainActivity).fromRealm
        viewModel.getCategories(fromRealm)
        (activity as MainActivity).fromRealm = true
    }

    override fun setUI() {
        binding.apply {
            setUpToolbar()
            setItemsVisible(visible = false)
            loading.root.isVisible
        }
    }

    private fun setUpToolbar() {
        (activity as MainActivity).apply {
            showBanner(visible = false)
            toolbarVisibility(visible = false)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoriesEvent.Categories -> setUpViewPager(event.result)
                is CategoriesEvent.SomethingWentWrong -> activity?.showErrorDialog()
            }
        }
    }

    private fun setUpViewPager(categories: List<Category>) {
        binding.apply {
            if (categories.isNotEmpty()) {
                adapter = ImageAdapter(categories, this@CategoryImagesFragment)
                viewpager.adapter = adapter
                viewpager.offscreenPageLimit = 1
                viewpager.clipToPadding = false
                viewpager.clipChildren = false
                viewpager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                setUpTransformer()

                TabLayoutMediator(tabLayout, viewpager) { _, _ -> }.attach()

                adapter?.updateTexts(0)
                viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        adapter?.updateTexts(position)
                    }
                })

                loading.root.isVisible = false
                doAnimations()
            }
        }
    }

    private fun doAnimations() {
        binding.apply {
            if ((activity as MainActivity).firstTime) {
                (activity as MainActivity).firstTime = false
                clTop.animateY(-200f, 10)
                clBottom.animateY(200f, 10)
                viewpager.animateX(300f, 10)
                countDown(1000) {
                    clTop.visibility = View.VISIBLE
                    clTop.animateY(0f, 400)
                    clBottom.visibility = View.VISIBLE
                    clBottom.animateY(0f, 400)
                    viewpager.visibility = View.VISIBLE
                    viewpager.animateX(0f, 400)
                }
            } else setItemsVisible(visible = true)
        }
    }

    private fun setItemsVisible(visible: Boolean) {
        binding.apply {
            clTop.isVisible = visible
            viewpager.isVisible = visible
            clBottom.isVisible = visible
        }
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, pos ->
            val r = 1 - abs(pos)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewpager.setPageTransformer(transformer)
    }

    override fun onCategoryScrolled(title: String, description: String) {
        binding.apply {
            tvTop.text = title
            tvBottom.text = description
        }
    }

    override fun onCategoryClick(id: String) {
        activity?.showFragmentDialog(CategoryDialog(id, this@CategoryImagesFragment))
    }

    override fun startGuesserGame(categoryId: String) =
        findNavController().navigate(actionCategoriesToGuesser(categoryId))

    override fun startCTemporaryGame(categoryId: String) =
        findNavController().navigate(actionCategoriesToTemporary(categoryId))

    override fun openMusicMaster() =
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.music_master_url))))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}