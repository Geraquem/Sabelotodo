package com.mmfsin.sabelotodo

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.sabelotodo.data.models.DataToDash
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

    override fun navigateToDashboard(dataToDash: DataToDash) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
            .replace(R.id.fragment_container, DashboardFragment(this, dataToDash))
            .addToBackStack(null)
            .commit()
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