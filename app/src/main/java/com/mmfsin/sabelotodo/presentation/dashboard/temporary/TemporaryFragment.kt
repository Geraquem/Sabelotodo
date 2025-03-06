package com.mmfsin.sabelotodo.presentation.dashboard.temporary

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentDashboardTemporaryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.dashboard.dialog.NoMoreQuestionsDialog
import com.mmfsin.sabelotodo.presentation.dashboard.temporary.dialog.LoserDialog
import com.mmfsin.sabelotodo.presentation.dashboard.temporary.interfaces.ITemporaryListener
import com.mmfsin.sabelotodo.presentation.models.ResultType
import com.mmfsin.sabelotodo.presentation.models.ResultType.BAD
import com.mmfsin.sabelotodo.presentation.models.ResultType.GOOD
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.BOTTOM
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.SAME_YEAR
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.TOP
import com.mmfsin.sabelotodo.utils.CATEGORY_ID
import com.mmfsin.sabelotodo.utils.animateX
import com.mmfsin.sabelotodo.utils.checkNotNulls
import com.mmfsin.sabelotodo.utils.countDown
import com.mmfsin.sabelotodo.utils.loadingCountDown
import com.mmfsin.sabelotodo.utils.showErrorDialog
import com.mmfsin.sabelotodo.utils.showFragmentDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemporaryFragment : BaseFragment<FragmentDashboardTemporaryBinding, TemporaryViewModel>(),
    ITemporaryListener {

    override val viewModel: TemporaryViewModel by viewModels()
    private lateinit var mContext: Context

    private var categoryId: String? = null
    private var category: Category? = null
    private var dataList: List<Data> = emptyList()

    private var position: Int = 0
    private var points: Int = 0
    private var record: Int = 0

    private var solution1: String? = null
    private var solution2: String? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardTemporaryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).inDashboard = true
        viewModel.checkIfTablet()
    }

    override fun getBundleArgs() {
        arguments?.let { categoryId = it.getString(CATEGORY_ID) }
    }

    override fun setUI() {
        binding.apply {
            setUpToolbar()
            showBanner(show = false)
            loading.root.isVisible = true
            scoreLayout.tvPoints.text = points.toString()
            imageOne.hideImage()
            imageTwo.hideImage()
        }
    }

    override fun setListeners() {
        binding.apply {
            tvOne.setOnClickListener { selectOption(TOP) }
            clImageOne.setOnClickListener { selectOption(TOP) }

            btnSameYear.setOnClickListener { selectOption(SAME_YEAR) }

            tvTwo.setOnClickListener { selectOption(BOTTOM) }
            clImageTwo.setOnClickListener { selectOption(BOTTOM) }
        }
    }

    private fun selectOption(option: TempSelectionType) {
        enableImages(false)
        checkNotNulls(solution1, solution2) { sol1, sol2 ->
            viewModel.checkSolutions(option, sol1, sol2)
        }
    }

    private fun setUpToolbar() {
        (activity as MainActivity).apply {
            toolbarIcon(showDuck = false)
            toolbarVisibility(visible = true)
        }
    }

    private fun showBanner(show: Boolean) {
        try {
            (activity as MainActivity).apply { showBanner(visible = show) }
        } catch (e: Exception) {
            Log.e("ACTIVITY", "Error hidding banner")
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is TemporaryEvent.ImageHeight -> {
                    setHeights(event.height)
                    categoryId?.let { viewModel.getCategory(it) } ?: run { error() }
                }

                is TemporaryEvent.GetCategory -> {
                    category = event.result
                    record = event.result.temporaryRecord ?: 0
                    setCategoryData()
                    viewModel.getDashboardData(event.result.id)
                }

                is TemporaryEvent.GuesserData -> {
                    dataList = event.data
                    setData()
                }

                is TemporaryEvent.Solution -> setSolution(event.solution)

                is TemporaryEvent.IsRecord -> {
                    if (event.result.isRecord) {
                        record = event.result.newRecord
                        binding.scoreLayout.tvRecord.text = record.toString()
                    }
                }

                is TemporaryEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setHeights(height: Int) {
        binding.apply {
            val layoutParamsOne = imageOne.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsOne.height = height
            imageOne.layoutParams = layoutParamsOne

            val layoutParamsTwo = imageTwo.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsTwo.height = height
            imageTwo.layoutParams = layoutParamsTwo
        }
    }

    private fun setCategoryData() {
        binding.apply {
            category?.let {
                Glide.with(requireContext()).load(it.duckImage).into(loading.image)
                (activity as MainActivity).toolbarText(it.toolbarText)
                setMainText(it.id)
                scoreLayout.tvRecord.text = it.temporaryRecord.toString()
            }
        }
    }

    private fun setMainText(id: String) {
        val text = when (id) {
            getString(R.string.id_spanish_age), getString(R.string.id_global_age) -> R.string.temporary_ages
            getString(R.string.id_films_series), getString(R.string.id_cartoon_creations), getString(
                R.string.id_videogames
            ) -> R.string.temporary_dates

            getString(R.string.id_important_dates) -> R.string.temporary_dates_important_dates
            else -> R.string.error_title
        }
        binding.tvTitle.text = getString(text)
    }

    private fun setData() {
        if (dataList.isNotEmpty()) {
            binding.apply {
                try {
                    val data = getData()
                    data?.let { d ->
                        val textOne = d.first.auxText?.let {
                            it + " " + d.first.secondText
                        } ?: run { d.first.secondText }
                        tvOne.text = textOne

                        solution1 = d.first.solution
                        setImage(d.first.image, imageOne)

                        val textTwo = d.second.auxText?.let {
                            it + " " + d.second.secondText
                        } ?: run { d.second.secondText }
                        tvTwo.text = textTwo

                        solution2 = d.second.solution
                        setImage(d.second.image, imageTwo, fromLeft = false)
                        loadingCountDown {
                            loading.root.isVisible = false
//                            showBanner(show = true)
                        }
                    }
                } catch (e: java.lang.Exception) {
                    error()
                }
            }
        } else error()
    }

    private fun getData(): Pair<Data, Data>? {
        return try {
            val data1 = dataList[position]
            position++
            if (position < dataList.size) {
                val data2 = dataList[position]
                Pair(data1, data2)
            } else {
                showNoMoreQuestions()
                null
            }
        } catch (e: Exception) {
            showNoMoreQuestions()
            null
        }
    }

    private fun setImage(image: String, view: ImageView, fromLeft: Boolean = true) {
        view.hideImage()
        Glide.with(this).load(image).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                animateImage(view, fromLeft)
                return false
            }
        }).into(view)
    }

    private fun animateImage(view: View, fromLeft: Boolean = true) {
        val position = if (fromLeft) -1000f else 1000f
        view.animateX(position, 10)
        countDown(100) {
            view.hideImage(false)
            view.animateX(0f, 300)
        }
    }

    private fun View.hideImage(hide: Boolean = true) {
        this.visibility = if (hide) View.INVISIBLE else View.VISIBLE
    }

    private fun enableImages(clickable: Boolean = true) {
        binding.apply {
            tvOne.isEnabled = clickable
            clImageOne.isEnabled = clickable
            btnSameYear.isEnabled = clickable
            tvTwo.isEnabled = clickable
            clImageTwo.isEnabled = clickable
        }
    }

    private fun setSolution(result: Pair<TempSelectionType, ResultType>) {
        binding.apply {
            val bgGood = setBackground(R.drawable.bg_temporary_good)
            val bgAlmost = setBackground(R.drawable.bg_temporary_almost)
            val bgBad = setBackground(R.drawable.bg_temporary_bad)

            when (result.second) {
                GOOD -> {
                    when (result.first) {
                        TOP -> cvOne.background = bgGood
                        BOTTOM -> cvTwo.background = bgGood
                        SAME_YEAR -> btnSameYear.background = bgGood
                    }
                    points++
                    scoreLayout.tvPoints.text = points.toString()
                    automaticContinue()
                }

                BAD -> {
                    when (result.first) {
                        TOP -> cvOne.background = bgBad
                        BOTTOM -> cvTwo.background = bgBad
                        SAME_YEAR -> btnSameYear.background = bgBad
                    }
                    countDown(750) {
                        activity?.showFragmentDialog(LoserDialog.newInstance(this@TemporaryFragment))
                    }
                }

                ResultType.SAME_YEAR -> {
                    btnSameYear.background = bgAlmost
                    automaticContinue()
                }

                /** Never this way */
                else -> {}
            }
        }
        category?.let { viewModel.checkRecord(points.toString(), record.toString(), it.id) }
    }

    private fun automaticContinue() {
        countDown(750) {
            binding.apply {
                enableImages()
                restartBackgrounds()

                position++
                if (position < dataList.size) setData()
                else {
                    (activity as MainActivity).inDashboard = false
                    activity?.let { NoMoreQuestionsDialog().show(it.supportFragmentManager, "") }
                }
                if (position % 20 == 0) (activity as MainActivity).showInterstitial()
            }
        }
    }

    private fun restartBackgrounds() {
        binding.apply {
            cvOne.background = setBackground(R.drawable.bg_temporary_neutro)
            btnSameYear.background = setBackground(R.drawable.bg_temporary_neutro)
            cvTwo.background = setBackground(R.drawable.bg_temporary_neutro)
        }
    }

    private fun setBackground(bg: Int) = getDrawable(mContext, bg)

    private fun showNoMoreQuestions() {
        (activity as MainActivity).inDashboard = false
        activity?.let { NoMoreQuestionsDialog().show(it.supportFragmentManager, "") }
    }

    override fun rematch() {
        category?.let {
            position = 0
            points = 0
            binding.scoreLayout.tvPoints.text = points.toString()
            viewModel.getDashboardData(it.id)
            enableImages()
            restartBackgrounds()
        } ?: run { error() }
    }

    override fun exitGame() {
        (activity as MainActivity).apply {
            inDashboard = false
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun error() {
        (activity as MainActivity).inDashboard = false
        activity?.showErrorDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}