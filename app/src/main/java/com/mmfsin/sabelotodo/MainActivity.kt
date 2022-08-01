package com.mmfsin.sabelotodo

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.sabelotodo.data.models.DataToDashDTO
import com.mmfsin.sabelotodo.data.models.RecordDTO
import com.mmfsin.sabelotodo.databinding.ActivityMainBinding
import com.mmfsin.sabelotodo.presentation.ICommunication
import com.mmfsin.sabelotodo.presentation.categories.CategoriesFragment
import com.mmfsin.sabelotodo.presentation.dashboard.DashboardFragment

class MainActivity : AppCompatActivity(), ICommunication {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CategoriesFragment(this))
            .addToBackStack(null)
            .commit()
    }

    override fun changeToolbarText(category: String) {
        binding.toolbarText.text = category
    }

    override fun navigateToDashboard(data: DataToDashDTO) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
            .replace(R.id.fragment_container, DashboardFragment(this, data))
            .addToBackStack(null)
            .commit()
    }

    override fun setNewRecord(record: RecordDTO) {
        TODO("Not yet implemented")
    }

    override fun somethingWentWrong() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(getString(R.string.oops))
            .setContentText(getString(R.string.somethingWentWrong))
            .show()
    }

    private fun sweetAlertGoBack() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.wannaExit))
            .setConfirmText(getString(R.string.yes))
            .setConfirmClickListener { sDialog ->
                changeToolbarText(getString(R.string.app_name))
                supportFragmentManager.popBackStack()
                sDialog.dismissWithAnimation()
            }
            .setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    override fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) finish() else sweetAlertGoBack()
    }
}