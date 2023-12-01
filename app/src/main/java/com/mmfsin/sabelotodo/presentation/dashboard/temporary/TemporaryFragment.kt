package com.mmfsin.sabelotodo.presentation.dashboard.temporary

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemporaryFragment : BaseFragment<FragmentDashboardTemporaryBinding, TemporaryViewModel>() {

    override val viewModel: TemporaryViewModel by viewModels()
    private lateinit var mContext: Context

    private var id: String? = null
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
        id?.let { viewModel.getCategory(it) } ?: run { error() }
    }

    override fun getBundleArgs() {
        arguments?.let { id = it.getString(CATEGORY_ID) }
    }

    override fun setUI() {
        binding.apply {
            setUpToolbar()
            loading.root.isVisible = true
//            scoreLayout.btnNext.visibility = View.INVISIBLE
            scoreLayout.scoreBoard.tvPoints.text = points.toString()
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

            scoreLayout.btnNext.setOnClickListener {
                btnNextClicked()
                if (position % 20 == 0) (activity as MainActivity).showInterstitial()
            }
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
            showBanner(visible = true)
            toolbarIcon(showDuck = false)
            toolbarVisibility(visible = true)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
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
                        binding.scoreLayout.scoreBoard.tvRecord.text = record.toString()
                    }
                }

                is TemporaryEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setCategoryData() {
        binding.apply {
            category?.let {
                Glide.with(requireContext()).load(it.duckImage).into(loading.image)
                (activity as MainActivity).toolbarText(it.toolbarText)
                setMainText(it.longitudePV)
                scoreLayout.btnNext.setColorFilter(Color.parseColor(it.colorDashboard))
                scoreLayout.scoreBoard.tvRecord.text = it.guesserRecord.toString()
            }
        }
    }

    private fun setMainText(length: Int) {
        val text = if (length < 4) R.string.temporary_ages else R.string.temporary_dates
        binding.tvTitle.text = getString(text)
    }

    private fun setData() {
        if (dataList.isNotEmpty()) {
            binding.apply {
                try {
                    val data = getData()
                    data?.let { d ->
                        tvOne.text = d.first.secondText
                        solution1 = d.first.solution
                        setImage(d.first.image, imageOne)
                        tvTwo.text = d.second.secondText
                        solution2 = d.second.solution
                        setImage(d.second.image, imageTwo, fromLeft = false)
                        loadingCountDown { loading.root.isVisible = false }
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
            tvTwo.isEnabled = clickable
            clImageTwo.isEnabled = clickable
        }
    }

    private fun setSolution(result: Pair<TempSelectionType, ResultType>) {
        binding.apply {
            val bgGood = getDrawable(mContext, R.drawable.bg_temporary_good)
            val bgAlmost = getDrawable(mContext, R.drawable.bg_temporary_almost)
            val bgBad = getDrawable(mContext, R.drawable.bg_temporary_bad)

            when (result.second) {
                GOOD -> {
                    when (result.first) {
                        TOP -> tvOne.background = bgGood
                        BOTTOM -> tvTwo.background = bgGood
                        SAME_YEAR -> tvSameYear.background = bgGood
                    }
                    points++
                    scoreLayout.scoreBoard.tvPoints.text = points.toString()
                    scoreLayout.btnNext.isVisible = true
                }

                BAD -> {
                    when (result.first) {
                        TOP -> tvOne.background = bgBad
                        BOTTOM -> tvTwo.background = bgBad
                        SAME_YEAR -> tvSameYear.background = bgBad
                    }
                    countDown(1000) {
                        Toast.makeText(mContext, "perdiste", Toast.LENGTH_SHORT).show()
                    }
                }

                ResultType.SAME_YEAR -> tvSameYear.background = bgAlmost

                else -> {
                    /** Shouldn't go this way */
                }
            }
        }
        category?.let { viewModel.checkRecord(points.toString(), record.toString(), it.id) }
    }

    private fun btnNextClicked() {
        binding.apply {
            enableImages()
//            scoreLayout.btnNext.visibility = View.INVISIBLE

            tvOne.background = null
            tvSameYear.background = getDrawable(mContext, R.drawable.bg_button_same_year)
            tvTwo.background = null

            position++
            if (position < dataList.size) setData()
            else {
                (activity as MainActivity).inDashboard = false
                activity?.let { NoMoreQuestionsDialog().show(it.supportFragmentManager, "") }
            }
            if (position % 20 == 0) (activity as MainActivity).showInterstitial()
        }
    }

    private fun showNoMoreQuestions() {
        (activity as MainActivity).inDashboard = false
        activity?.let { NoMoreQuestionsDialog().show(it.supportFragmentManager, "") }
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