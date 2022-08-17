package com.mmfsin.sabelotodo.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.data.models.DataToDashDTO
import com.mmfsin.sabelotodo.data.models.RecordDTO
import com.mmfsin.sabelotodo.databinding.ActivityMainBinding
import com.mmfsin.sabelotodo.presentation.ICommunication
import com.mmfsin.sabelotodo.presentation.categories.CategoriesFragment
import com.mmfsin.sabelotodo.presentation.dashboard.DashboardFragment

class MainActivity : AppCompatActivity(), ICommunication {

    private lateinit var binding: ActivityMainBinding

    private val helper by lazy { MainHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goBack.setOnClickListener { onBackPressed() }
        binding.share.setOnClickListener { startActivity(helper.shareInfo()) }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CategoriesFragment(this))
            .addToBackStack(null)
            .commit()
    }

    override fun changeToolbarText(category: String) {
        binding.toolbarText.text = category
        binding.goBack.visibility = View.VISIBLE
        binding.share.visibility = View.GONE
    }

    override fun navigateToDashboard(data: DataToDashDTO) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
            .replace(R.id.fragment_container, DashboardFragment(this, data))
            .addToBackStack(null)
            .commit()
    }

    override fun getRecord(category: String): Int = helper.getRecord(category)

    override fun setNewRecord(record: RecordDTO) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(record.category, record.newRecord)
            apply()
        }
    }

    override fun notMoreQuestions() {
        SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setCustomImage(R.drawable.ic_sad_face)
            .setTitleText(getString(R.string.noMoreQuestions))
            .setConfirmText(getString(R.string.ok))
            .setConfirmClickListener { sDialog ->
                changeToolbarText(getString(R.string.app_name))
                binding.goBack.visibility = View.GONE
                supportFragmentManager.popBackStack()
                sDialog.dismissWithAnimation()
            }.show()
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
                binding.goBack.visibility = View.GONE
                binding.share.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                sDialog.dismissWithAnimation()
            }.setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
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