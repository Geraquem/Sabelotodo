package com.mmfsin.sabelotodo.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.databinding.ActivityMainBinding
import com.mmfsin.sabelotodo.domain.models.DataToDashDTO
import com.mmfsin.sabelotodo.domain.models.RecordDTO
import com.mmfsin.sabelotodo.presentation.ICommunication
import com.mmfsin.sabelotodo.presentation.categories.CategoriesFragment
import com.mmfsin.sabelotodo.presentation.dashboard.DashboardFragment
import io.realm.Realm
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), ICommunication {

    private lateinit var binding: ActivityMainBinding

    private val helper by lazy { MainHelper(this) }

    private var isDuckButton by Delegates.notNull<Boolean>()

    private var mInterstitialAd: InterstitialAd? = null
    private val mInterstitalId = "ca-app-pub-3940256099942544/1033173712" //Pruebas
//    private val mInterstitalId = "ca-app-pub-4515698012373396/9980090620" //Real

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(500)
        setTheme(R.style.Theme_Sabelotodo)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)
        MobileAds.initialize(this) {}

        setUI()
        setListeners()
    }

    private fun setUI() {
        binding.apply {
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
            loadInterstitial(AdRequest.Builder().build())

            setToolbarIcon(true)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CategoriesFragment(this@MainActivity))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setListeners() {
        binding.apply {
            share.setOnClickListener { startActivity(helper.shareInfo()) }

            toolbarIcon.setOnClickListener {
                if (isDuckButton) //showToast()
                else onBackPressed()
            }
        }
    }

    override fun changeToolbarText(category: String) {
        setToolbarIcon(false)
        binding.toolbarText.text = category
        binding.share.visibility = View.GONE
    }

    override fun navigateToDashboard(data: DataToDashDTO) {
        supportFragmentManager.beginTransaction()
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
                setToolbarIcon(false)
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
        SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setCustomImage(R.drawable.ic_pato_exit)
            .setTitleText(getString(R.string.wannaExit))
            .setConfirmText(getString(R.string.yes))
            .setConfirmClickListener { sDialog ->
                changeToolbarText(getString(R.string.app_name))
                setToolbarIcon(true)
                binding.share.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                sDialog.dismissWithAnimation()
            }
            .setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    private fun setToolbarIcon(duck: Boolean) {
        isDuckButton = duck
        val image =
            if (duck) R.drawable.ic_pato
            else R.drawable.ic_return

        binding.toolbarIcon.setImageResource(image)
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

    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(this, mInterstitalId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                loadInterstitial(AdRequest.Builder().build())
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun showAd(pos: Int) {
        if ((pos % 20) == 0 && mInterstitialAd != null) {
            mInterstitialAd!!.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }

    @SuppressLint("InflateParams")
    private fun showToast() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}