package com.mmfsin.sabelotodo.presentation.dashboard

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentDashboardBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.ResultType
import com.mmfsin.sabelotodo.domain.models.ResultType.*
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.dashboard.dialog.NoMoreQuestionsDialog
import com.mmfsin.sabelotodo.utils.CATEGORY_ID
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel by viewModels()
    private lateinit var mContext: Context

    private var id: String? = null
    private var category: Category? = null
    private var dataList: List<Data> = emptyList()

    private var currentSolution: String = ""
    private var pinViewLength: Int = 0
    private var position: Int = 0
    private var points: Int = 0
    private var record: Int = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

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
            loading.root.isVisible
            solution.root.isVisible = false
            scoreBoard.tvPoints.text = points.toString()
        }
    }

    override fun setListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                val answer = pvResponse.text.toString()
                if (answer.length == pinViewLength) {
                    btnCheck.isEnabled = false
                    viewModel.checkSolution(answer, currentSolution)
                }
            }
            btnNext.setOnClickListener {
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

    private fun setUpToolbar() {
        (activity as MainActivity).apply {
            showBanner(visible = true)
            toolbarIcon(showDuck = false)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DashboardEvent.GetCategory -> {
                    category = event.result
                    record = event.result.record ?: 0
                    setCategoryData()
                    viewModel.getDashboardData(event.result.id)
                }
                is DashboardEvent.DashboardData -> {
                    dataList = event.data.shuffled()
                    setData()
                }
                is DashboardEvent.Solution -> setSolution(event.solution)
                is DashboardEvent.IsRecord -> {
                    if (event.result.isRecord) {
                        record = event.result.newRecord
                        binding.scoreBoard.tvRecord.text = record.toString()
                    }
                }
                is DashboardEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setCategoryData() {
        binding.apply {
            category?.let {
                (activity as MainActivity).toolbarText(it.title)
                setPinView(it.longitudePV)
                val colorDashboard = Color.parseColor(it.colorDashboard)
                btnCheck.background.setTint(colorDashboard)
                btnNext.setColorFilter(colorDashboard)
                scoreBoard.tvRecord.text = it.record.toString()
            }
        }
    }

    private fun setPinView(length: Int) {
        binding.apply {
            pinViewLength = length
            pvResponse.itemCount = pinViewLength
            if (length == 2) tvInThe.isVisible = false else tvYears.isVisible = false
            if (length == 2) solution.tvInThe.isVisible = false else solution.tvYears.isVisible =
                false
            pvResponse.addTextChangedListener(textWatcher)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (binding.pvResponse.text.toString().length == pinViewLength) {
                (activity as MainActivity).closeKeyboard()
            }
        }
    }

    private fun setData() {
        if (dataList.isNotEmpty()) {
            binding.apply {
                try {
                    val data = dataList[position]
                    solution.root.isVisible = false
                    btnCheck.isEnabled = true
                    currentSolution = data.solution
                    Glide.with(mContext).load(data.image).into(image)
                    pvResponse.text = null
                    tvFirstText.text = data.firstText
                    tvSecondText.text = data.secondText
                    solution.tvCorrectAnswer.text = currentSolution
                    animateProgress(solution.progressBarLeft, 0, 0)
                    animateProgress(solution.progressBarRight, 0, 0)
                    loading.root.isVisible = false
                } catch (e: java.lang.Exception) {
                    error()
                }
            }
        } else error()
    }

    private fun setSolution(result: ResultType) {
        binding.apply {
            solution.apply {
                when (result) {
                    GOOD -> {
                        points += 2
                        tvPoints.text = getString(R.string.solution_correct_answer)
                        tvPoints.setTextColor(getColor(mContext, R.color.color_good))
                    }
                    ALMOST_GOOD -> {
                        points += 1
                        tvPoints.text = getString(R.string.solution_almost_good_answer)
                        tvPoints.setTextColor(getColor(mContext, R.color.color_almost))
                    }
                    BAD -> {
                        points -= 1
                        tvPoints.text = getString(R.string.solution_bad_answer)
                        tvPoints.setTextColor(getColor(mContext, R.color.color_bad))
                    }
                }
                root.isVisible = true
                animateProgress(solution.progressBarLeft, 100, 100)
                animateProgress(solution.progressBarRight, 100, 100)
            }
            scoreBoard.tvPoints.text = points.toString()
            category?.let { viewModel.checkRecord(points.toString(), record.toString(), it.id) }
        }
    }

    private fun animateProgress(progress: ProgressBar, total: Int, votes: Int) {
        progress.max = total * 100
        val animation = ObjectAnimator.ofInt(progress, "progress", votes * 100)
        animation.duration = 2000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
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