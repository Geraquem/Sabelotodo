package com.mmfsin.sabelotodo.presentation.dashboard

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.databinding.FragmentDashboardBinding
import com.mmfsin.sabelotodo.domain.models.*
import com.mmfsin.sabelotodo.domain.models.ResultType.*
import com.mmfsin.sabelotodo.presentation.ICommunication
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class DashboardFragment(
    private val listener: ICommunication, private val data: DataToDashDTO
) : Fragment(), DashboardView {

    private var _bdg: FragmentDashboardBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { DashboardPresenter(this) }

    private lateinit var mContext: Context

    private lateinit var completedList: List<DataDTO>

    private var pos = 0
    private var longitude = 0
    private lateinit var correctAnswer: String

    private var solutionFlipped: Boolean = false

    private var customColor = 0

    private var mPoints = 0
    private var actualRecord by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        presenter.getData(data.category)
        presenter.getCategoryDuck(data.category)
            ?.let { Glide.with(mContext).load(it).into(binding.imgBackground) }
        onClick()
    }

    override fun dataListFilled(list: List<DataDTO>) {
        completedList = list
        setQuestionData(completedList[pos])
    }

    private fun init() {
        actualRecord = data.actualRecord
        listener.changeToolbarText(presenter.toolbarText(mContext, data.category))
        longitude = presenter.checkPinViewLongitude(mContext, data.category)

        val colorByCategory = presenter.getColorByCategory(mContext, data.category)
        customColor = getColor(mContext, colorByCategory)

        with(binding) {
            loading.root.visibility = View.VISIBLE

            changeSolutionFront(solutionFlip.solutionFront.bg, customColor)
            changeSolutionBack(solutionFlip.solutionBack.bg, customColor)
            check.background.setTint(customColor)

            next.setColorFilter(customColor)

            response.addTextChangedListener(textWatcher)
            response.itemCount = longitude

            solutionFlip.flip.isFlipOnTouch = false

            scoreBoard.actualRecord.text =
                getString(R.string.actualRecord, data.actualRecord.toString())
        }
    }

    private fun changeSolutionFront(view: View, color: Int) {
        val background = view.background
        if (background is GradientDrawable) {
            background.setColor(color)
            background.setStroke(0, color)
            view.background = background
        }
    }

    private fun changeSolutionBack(view: View, color: Int) {
        val background = view.background
        if (background is GradientDrawable) {
            background.setStroke(10, color)
            background.setColor(Color.WHITE)
            view.background = background
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (presenter.checkPinViewLongitude(longitude, binding.response.text.toString())) {
                listener.closeKeyboard()
            }
        }
    }

    private fun onClick() {
        binding.apply {
            llNext.setOnClickListener {
                pos++
                if (pos < completedList.size) {
                    loading.root.visibility = View.VISIBLE

                    if (solutionFlipped) flipSolution()

                    check.isEnabled = true
                    response.isEnabled = true
                    response.text = null
                    setQuestionData(completedList[pos])

                } else {
                    listener.notMoreQuestions()
                    llNext.visibility = View.GONE
                }
                showAd()
            }

            check.setOnClickListener {
//            pos--
//            presenter.getQuestionData(data.category, questionNames[pos])

                val response = response.text.toString()
                if (response.isNotEmpty() && response.length == longitude) {
                    check.isEnabled = false
                    binding.response.isEnabled = false
                    listener.closeKeyboard()
                    presenter.checkSolution(SolutionDTO(correctAnswer, response))
                    flipSolution()
                }
            }
        }
    }

    private fun setQuestionData(data: DataDTO) {
        correctAnswer = presenter.checkSolution(data.solution)
        val splitText = data.text.split("%%%")
        with(binding) {
            text.text = splitText[0]
            mainText.text = splitText[1]
            presenter.checkDescription(data.description)
            Picasso.get().load(data.image).into(image);
        }
        binding.loading.root.visibility = View.GONE
    }

    override fun handleDescription(enable: Boolean, description: String) {
        if (enable) {
            binding.description.text = description
            binding.description.visibility = View.VISIBLE
        } else binding.description.visibility = View.GONE
    }

    override fun setTwoLongitudePinView() {
        binding.inThe.visibility = View.GONE
        binding.years.visibility = View.VISIBLE
    }

    override fun showSolution(solution: String, type: ResultType) {
        val correctAnswer = binding.solutionFlip.solutionBack.correctAnswer
        val solutionPoints = binding.solutionFlip.solutionBack.tvPoints
        when (type) {
            GOOD -> {
                mPoints += 2
                solutionPoints.setTextColor(
                    resources.getColor(R.color.goodPhrase, null)
                )
                solutionPoints.text = getString(R.string.correct_answer)
            }
            ALMOST_GOOD -> {
                mPoints += 1
                solutionPoints.setTextColor(
                    resources.getColor(R.color.almostGoodPhrase, null)
                )
                solutionPoints.text = getString(R.string.almost_good_answer)
            }
            BAD -> {
                mPoints -= 1
                solutionPoints.setTextColor(
                    resources.getColor(R.color.badPhrase, null)
                )
                solutionPoints.text = getString(R.string.bad_answer)
            }
        }

        if (mPoints > actualRecord) {
            actualRecord = mPoints
            binding.scoreBoard.actualRecord.text =
                getString(R.string.actualRecord, actualRecord.toString())
            listener.setNewRecord(RecordDTO(data.category, mPoints))
        }
        binding.scoreBoard.points.text = mPoints.toString()
        correctAnswer.text = when (longitude) {
            2 -> getString(R.string.has_years, solution)
            else -> getString(R.string.was_in, solution)
        }
    }

    private fun flipSolution() {
        solutionFlipped = !solutionFlipped
        binding.solutionFlip.flip.flipTheView()
    }

    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun showAd() = listener.showAd(pos)

}