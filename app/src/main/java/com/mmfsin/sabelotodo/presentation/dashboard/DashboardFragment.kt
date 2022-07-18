package com.mmfsin.sabelotodo.presentation.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.databinding.FragmentDashboardBinding
import com.mmfsin.sabelotodo.presentation.ICommunication

class DashboardFragment(private val listener: ICommunication, private val category: String) :
    Fragment(), DashboardView {

    private var _bdg: FragmentDashboardBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { DashboardPresenter(this) }

    private lateinit var mContext: Context

    private lateinit var questionNames: List<String>
    private var pos = 0

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
        presenter.getDataList(category)
    }

    override fun setDataList(list: List<String>) {
        questionNames = list
        presenter.getQuestionData(category, questionNames[pos])
    }

    override fun setQuestionData(data: DataDTO) {
        val a = 2
        /**
         *
         *
         *
         * AQUI
         *
         *
         */

        Toast.makeText(mContext, data.text, Toast.LENGTH_SHORT).show()

        println(" --------------------------- " + data.toString())
    }

    override fun somethingWentWrong() = listener.somethingWentWrong()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}