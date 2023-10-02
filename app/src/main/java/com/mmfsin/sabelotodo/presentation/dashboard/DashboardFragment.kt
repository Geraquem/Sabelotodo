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
import com.mmfsin.sabelotodo.presentation.models.SolutionType
import com.mmfsin.sabelotodo.presentation.models.SolutionType.AGES
import com.mmfsin.sabelotodo.presentation.models.SolutionType.DATES
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

    private var solutionType: SolutionType? = null

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
            llSolutions.isVisible = false
            scoreBoard.tvPoints.text = points.toString()
        }
    }

    override fun setListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                val answer = pvResponse.text.toString()
                if (answer.length == pinViewLength) {
                    pvResponse.isEnabled = false
                    btnCheck.isEnabled = false
                    setButtonColor(null)
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
                    dataList = event.data
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
                (activity as MainActivity).toolbarText(it.toolbarText)
                setPinView(it.longitudePV)
                btnNext.setColorFilter(Color.parseColor(it.colorDashboard))
                scoreBoard.tvRecord.text = it.record.toString()
            }
        }
    }

    private fun setPinView(length: Int) {
        binding.apply {
            pinViewLength = length
            pvResponse.itemCount = pinViewLength
            /** AGES */
            if (length == 2) {
                solutionType = AGES
                solutionDate.root.visibility = View.GONE
                tvInThe.isVisible = false
            }
            /** DATES */
            else {
                solutionType = DATES
                solutionAge.root.visibility = View.GONE
                tvYears.isVisible = false
            }
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
                    llSolutions.isVisible = false
                    setBirth(data.birth)
                    currentSolution = data.solution
                    Glide.with(mContext).load(data.image).into(image)
                    pvResponse.text = null
                    pvResponse.isEnabled = true
                    btnCheck.isEnabled = true
                    tvFirstText.text = data.firstText
                    tvSecondText.text = data.secondText
                    category?.let { setButtonColor(Color.parseColor(it.colorDashboard)) }
                    resetSolution()
                    loading.root.isVisible = false
                } catch (e: java.lang.Exception) {
                    error()
                }
            }
        } else error()
    }

    private fun setBirth(birth: List<String>) {
        if (birth.isNotEmpty() && birth.size == 3 && solutionType == AGES) {
            val of = getString(R.string.solution_age_of)
            val birthText = "${birth[0]} $of ${birth[1]} $of ${birth[2]}"
            binding.solutionAge.tvBirthDate.text = birthText
        }
    }

    private fun resetSolution() {
        binding.apply {
            when (solutionType) {
                AGES -> {
                    solutionAge.tvCorrectAnswer.text = currentSolution
                    animateProgress(solutionAge.progressBarLeft, 0, 0)
                    animateProgress(solutionAge.progressBarRight, 0, 0)
                }
                DATES -> {
                    solutionDate.tvCorrectAnswer.text = currentSolution
                    animateProgress(solutionDate.progressBarLeft, 0, 0)
                    animateProgress(solutionDate.progressBarRight, 0, 0)
                }
                /** if null */
                else -> {}
            }
        }
    }

    private fun setButtonColor(color: Int?) {
        binding.btnCheck.apply {
            color?.let {
                setTextColor(getColor(mContext, R.color.black))
                elevation = 5f
                background.setTint(it)
            } ?: run {
                setTextColor(getColor(mContext, R.color.text_button_disabled))
                elevation = 0f
                background.setTint(getColor(mContext, R.color.button_disabled))
            }
        }
    }

    private fun setSolution(result: ResultType) {
        binding.apply {
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

            when (solutionType) {
                AGES -> {
                    llSolutions.isVisible = true
                    animateProgress(solutionAge.progressBarLeft, 100, 100)
                    animateProgress(solutionAge.progressBarRight, 100, 100)

                }
                DATES -> {
                    llSolutions.isVisible = true
                    animateProgress(solutionDate.progressBarLeft, 100, 100)
                    animateProgress(solutionDate.progressBarRight, 100, 100)
                }
                /**if null*/
                else -> {}
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