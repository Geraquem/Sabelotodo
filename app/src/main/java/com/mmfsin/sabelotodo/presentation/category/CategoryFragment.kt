package com.mmfsin.sabelotodo.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.databinding.FragmentDashboardBinding

class CategoryFragment : Fragment(), DashboardView {

    private var _bdg: FragmentDashboardBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { CategoryPresenter(this) }

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
        presenter.getData()
    }


    override fun setData(data: DataDTO) {

    }

    override fun aaa() {
        Toast.makeText(requireActivity(), " WORKS" , Toast.LENGTH_SHORT).show()
    }
    override fun somethingWentWrong() {
        Toast.makeText(requireActivity(), " NOT WORKS" , Toast.LENGTH_SHORT).show()
    }
}