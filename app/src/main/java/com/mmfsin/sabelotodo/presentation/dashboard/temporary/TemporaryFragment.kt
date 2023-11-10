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
import com.mmfsin.sabelotodo.domain.models.ResultType
import com.mmfsin.sabelotodo.domain.models.ResultType.GOOD
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.dashboard.dialog.NoMoreQuestionsDialog
import com.mmfsin.sabelotodo.presentation.dashboard.temporary.TemporaryFragment.Stays.STAYS_BOTTOM
import com.mmfsin.sabelotodo.presentation.dashboard.temporary.TemporaryFragment.Stays.STAYS_TOP
import com.mmfsin.sabelotodo.presentation.models.SolutionType
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.BOTTOM
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

    private var solutionType: SolutionType? = null
    private var stays: Stays? = null

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
            scoreLayout.btnNext.visibility = View.INVISIBLE
            scoreLayout.scoreBoard.tvPoints.text = points.toString()
            imageOne.hideImage()
            imageTwo.hideImage()
        }
    }

    override fun setListeners() {
        binding.apply {
            llTop.setOnClickListener {
                stays = STAYS_TOP
                checkNotNulls(solution1, solution2) { sol1, sol2 ->
                    viewModel.checkSolutions(TOP, sol1, sol2)
                }
            }

            llBottom.setOnClickListener {
                stays = STAYS_BOTTOM
                checkNotNulls(solution1, solution2) { sol1, sol2 ->
                    viewModel.checkSolutions(BOTTOM, sol1, sol2)
                }
            }

            scoreLayout.btnNext.setOnClickListener {
                btnNextClicked()
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
                is TemporaryEvent.GetCategory -> {
                    category = event.result
                    record = event.result.record ?: 0
                    setCategoryData()
                    viewModel.getDashboardData(event.result.id)
                }

                is TemporaryEvent.GuesserData -> {
                    dataList = event.data
                    setFirstData()
                }

                is TemporaryEvent.Solution -> setSolution(event.solution)

                is TemporaryEvent.IsRecord -> {
//                    if (event.result.isRecord) {
//                        record = event.result.newRecord
//                        binding.scoreBoard.tvRecord.text = record.toString()
//                    }
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
                setSolutionType(it.longitudePV)
                scoreLayout.btnNext.setColorFilter(Color.parseColor(it.colorDashboard))
                scoreLayout.scoreBoard.tvRecord.text = it.record.toString()
            }
        }
    }

    private fun setSolutionType(length: Int) {
        solutionType = if (length < 4) SolutionType.AGES else SolutionType.DATES
    }

    private fun setFirstData() {
        if (dataList.isNotEmpty()) {
            binding.apply {
                try {
                    val data = getFirstData()
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

    private fun getFirstData(): Pair<Data, Data>? {
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
            Toast.makeText(mContext, e.message.toString(), Toast.LENGTH_SHORT).show()
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

    private fun setSolution(result: Pair<TempSelectionType, ResultType>) {
        binding.apply {
            when (result.second) {
                GOOD -> {
                    when (result.first) {
                        TOP -> imageOne.setBackgroundResource(R.drawable.bg_image_win)
                        BOTTOM -> imageTwo.setBackgroundResource(R.drawable.bg_image_win)
                    }
                    scoreLayout.btnNext.isVisible = true
                }

                else -> {
                    when (result.first) {
                        TOP -> imageOne.setBackgroundResource(R.drawable.bg_image_loose)
                        BOTTOM -> imageTwo.setBackgroundResource(R.drawable.bg_image_loose)
                    }
                    countDown(1000) {
                        Toast.makeText(mContext, "perdiste", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun btnNextClicked() {
        binding.apply {
            scoreLayout.btnNext.visibility = View.INVISIBLE

            imageOne.setBackgroundResource(0)
            imageTwo.setBackgroundResource(0)

            when (stays) {
                STAYS_TOP -> {
                    solution2 = null
                    setSingleData(STAYS_TOP)
                }

                STAYS_BOTTOM -> {
                    solution1 = null
                    setSingleData(STAYS_BOTTOM)
                }

                else -> error()
            }
        }
    }

    private fun setSingleData(image: Stays) {
        binding.apply {
            position++
            if (position < dataList.size) {
                val newData = dataList[position]
                when (image) {
                    STAYS_TOP -> {
                        tvTwo.text = newData.secondText
                        solution2 = newData.solution
                        setImage(newData.image, imageTwo, fromLeft = false)
                    }

                    STAYS_BOTTOM -> {
                        tvOne.text = newData.secondText
                        solution1 = newData.solution
                        setImage(newData.image, imageOne)
                    }
                }
            } else showNoMoreQuestions()
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

    enum class Stays {
        STAYS_TOP, STAYS_BOTTOM
    }
}