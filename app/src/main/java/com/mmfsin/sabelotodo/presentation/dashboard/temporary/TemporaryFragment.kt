package com.mmfsin.sabelotodo.presentation.dashboard.temporary

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.base.BaseFragment
import com.mmfsin.sabelotodo.databinding.FragmentDashboardTemporaryBinding
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.presentation.MainActivity
import com.mmfsin.sabelotodo.presentation.dashboard.dialog.NoMoreQuestionsDialog
import com.mmfsin.sabelotodo.presentation.models.SolutionType
import com.mmfsin.sabelotodo.utils.CATEGORY_ID
import com.mmfsin.sabelotodo.utils.countDown
import com.mmfsin.sabelotodo.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemporaryFragment : BaseFragment<FragmentDashboardTemporaryBinding, TemporaryViewModel>() {

    override val viewModel: TemporaryViewModel by viewModels()
    private lateinit var mContext: Context

    private var id: String? = null
    private var category: Category? = null
    private var dataList: List<Data> = emptyList()

    private var firstAccess = true

    private var position: Int = 0
    private var points: Int = 0
    private var record: Int = 0

    private var solutionType: SolutionType? = null

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
            loading.root.isVisible
            scoreBoard.tvPoints.text = points.toString()
        }
    }

    override fun setListeners() {
        binding.apply {


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
                is TemporaryEvent.GetCategory -> {
                    category = event.result
                    record = event.result.record ?: 0
                    setCategoryData()
                    viewModel.getDashboardData(event.result.id)
                }

                is TemporaryEvent.GuesserData -> {
                    dataList = event.data
                    setData()
                }

                is TemporaryEvent.Solution -> {}
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
                btnNext.setColorFilter(Color.parseColor(it.colorDashboard))
                scoreBoard.tvRecord.text = it.record.toString()
            }
        }
    }

    private fun setData() {
        if (dataList.isNotEmpty()) {
            binding.apply {
                try {
                    /** TODO */

                    if (firstAccess) {
                        firstAccess = false
                        countDown { loading.root.isVisible = false }
                    } else loading.root.isVisible = false
                } catch (e: java.lang.Exception) {
                    error()
                }
            }
        } else error()
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