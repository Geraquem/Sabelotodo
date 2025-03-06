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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentCategoriesBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.categories.CategoriesFragmentDirections.Companion.actionCategoriesToGuesser
import com.mmfsin.sabelotodo.presentation.categories.CategoriesFragmentDirections.Companion.actionCategoriesToTemporary
import com.mmfsin.sabelotodo.presentation.categories.adapter.CategoriesAdapter
import com.mmfsin.sabelotodo.presentation.categories.dialogs.CuackDialog
import com.mmfsin.sabelotodo.presentation.categories.dialogs.category.CategoryBSheet
import com.mmfsin.sabelotodo.presentation.categories.dialogs.category.MusicDialog
import com.mmfsin.sabelotodo.presentation.categories.interfaces.ICategoryListener
import com.mmfsin.sabelotodo.utils.animateY
import com.mmfsin.sabelotodo.utils.countDown
import com.mmfsin.sabelotodo.utils.showErrorDialog
import com.mmfsin.sabelotodo.utils.showFragmentDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>(),
    ICategoryListener {

    override val viewModel: CategoriesViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

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
            loadNativeAds()
            loading.root.isVisible
        }
    }

    override fun setListeners() {
        binding.apply {
            ivDuck.setOnClickListener { activity?.showFragmentDialog(CuackDialog()) }
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
                is CategoriesEvent.Categories -> setCategoryRecycler(event.result)
                is CategoriesEvent.SomethingWentWrong -> activity?.showErrorDialog()
            }
        }
    }

    override fun onCategoryScrolled(category: Category) {}

    private fun setCategoryRecycler(categories: List<Category>) {
        if (categories.isNotEmpty()) {
            binding.rvCategory.apply {
                (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                layoutManager = LinearLayoutManager(mContext)
                adapter = CategoriesAdapter(categories, this@CategoriesFragment)
            }
            binding.loading.root.isVisible = false
            doAnimations()
        }
    }

    private fun doAnimations() {
        binding.apply {
            if ((activity as MainActivity).firstTime) {
                (activity as MainActivity).firstTime = false
                llTop.animateY(-200f, 10)
                rvCategory.animateY(1500f, 10)
                countDown(500) {
                    llTop.visibility = View.VISIBLE
                    llTop.animateY(0f, 500)
                    rvCategory.visibility = View.VISIBLE
                    rvCategory.animateY(0f, 1000)
                    nativeAd.isVisible = true
                }
            } else setItemsVisible(visible = true)
        }
    }

    private fun setItemsVisible(visible: Boolean) {
        binding.apply {
            llTop.isVisible = visible
            rvCategory.isVisible = visible
            nativeAd.isVisible = visible
        }
    }

    override fun onCategoryClick(id: String) {
        activity?.showFragmentDialog(CategoryBSheet(id, this@CategoriesFragment))
    }

    override fun startGuesserGame(categoryId: String) =
        findNavController().navigate(actionCategoriesToGuesser(categoryId))

    override fun startTemporaryGame(categoryId: String) =
        findNavController().navigate(actionCategoriesToTemporary(categoryId))

    override fun openMusicMasterDialog(categoryId: String) {
        activity?.showFragmentDialog(
            MusicDialog.newInstance(categoryId, this@CategoriesFragment)
        )
    }

    override fun openMusicMaster() =
        startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.music_master_url))))

    private fun loadNativeAds() {
        val adLoader =
            AdLoader.Builder(mContext, getString(R.string.nativo)).forNativeAd { nativeAd ->
                populateNativeAdView(nativeAd, binding.nativeAd)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    binding.nativeAd.isVisible = false
                }
            }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        binding.nativeContent.apply {
            tvTitle.text = nativeAd.headline
            tvDescription.text = nativeAd.body
            val icon = nativeAd.icon
            icon?.let {
                image.setImageDrawable(icon.drawable)
                image.isVisible = true
            } ?: run { image.isVisible = false }

            actionBtn.text = nativeAd.callToAction
            actionBtn.isVisible = !nativeAd.callToAction.isNullOrBlank()
            adView.callToActionView = actionBtn

            binding.nativeAd.isVisible = true
            adView.setNativeAd(nativeAd)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}