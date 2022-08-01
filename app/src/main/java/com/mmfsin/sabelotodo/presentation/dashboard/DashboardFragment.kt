package com.mmfsin.sabelotodo.presentation.dashboard

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.models.SolutionDTO
import com.mmfsin.sabelotodo.databinding.FragmentDashboardBinding
import com.mmfsin.sabelotodo.presentation.ICommunication
import com.squareup.picasso.Picasso

class DashboardFragment(
    private val listener: ICommunication,
    private val category: CategoryDTO
) :
    Fragment(), DashboardView {

    private var _bdg: FragmentDashboardBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { DashboardPresenter(this) }

    private lateinit var mContext: Context

    private lateinit var questionNames: List<String>
    private var pos = 0
    private var longitude = 0
    private lateinit var correctAnswer: String

    private var mPoints = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        presenter.getDataList(category.name)
        Glide.with(mContext).load(category.image).into(binding.initialImage.image)
        object : CountDownTimer(1750, 100) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.initialImage.root.visibility = View.GONE
            }
        }.start()
        onClick()
    }

    private fun init() {
        listener.changeToolbarText(category.title)
        longitude = presenter.checkPinViewLongitude(mContext, category.name)
        with(binding) {
            response.addTextChangedListener(textWatcher)
            response.itemCount = longitude
            loading.root.visibility = View.VISIBLE
            initialImage.root.visibility = View.VISIBLE
            solution.root.visibility = View.GONE
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
        binding.next.setOnClickListener {
            pos++
            if (pos < questionNames.size) {
                binding.loading.root.visibility = View.VISIBLE
                binding.solution.root.visibility = View.GONE
                binding.check.isEnabled = true
                binding.response.isEnabled = true
                binding.response.text = null
                presenter.getQuestionData(category.name, questionNames[pos])
            } else {
                /** sweet alert */
                Toast.makeText(mContext, "no hay más preguntas", Toast.LENGTH_SHORT).show()
                binding.next.visibility = View.GONE
            }
        }

        binding.check.setOnClickListener {
            val response = binding.response.text.toString()
            if (response.isNotEmpty() && response.length == longitude) {
                binding.check.isEnabled = false
                binding.response.isEnabled = false
                listener.closeKeyboard()
                presenter.checkSolution(SolutionDTO(correctAnswer, response))
            }
        }
    }

    override fun setDataList(list: List<String>) {
        questionNames = list
        presenter.getQuestionData(category.name, questionNames[pos])
    }

    override fun setQuestionData(data: DataDTO) {
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

    override fun showSolution(solution: String, isCorrect: Int) {
        when (isCorrect) {
            0 -> {
                mPoints += 2
                binding.solution.typeAnswer.setTextColor(
                    resources.getColor(R.color.goodPhrase, null)
                )
                binding.solution.typeAnswer.text = getString(R.string.correct_answer)
            }
            1 -> {
                mPoints += 1
                binding.solution.typeAnswer.setTextColor(
                    resources.getColor(R.color.almostPhrase, null)
                )
                binding.solution.typeAnswer.text = getString(R.string.almost_answer)
            }
            2 -> {
                mPoints -= 1
                binding.solution.typeAnswer.setTextColor(
                    resources.getColor(R.color.badPhrase, null)
                )
                binding.solution.typeAnswer.text = getString(R.string.bad_answer)
            }
        }
        binding.scoreBoard.points.text = mPoints.toString()
        binding.solution.root.visibility = View.VISIBLE
        binding.solution.correctAnswer.text = when (longitude) {
            2 -> getString(R.string.has_years, solution)
            else -> getString(R.string.was_in, solution)
        }
    }

    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}