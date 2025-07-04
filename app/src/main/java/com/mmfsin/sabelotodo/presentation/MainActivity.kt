package com.mmfsin.sabelotodo.presentation

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.databinding.ActivityMainBinding
import com.mmfsin.sabelotodo.presentation.categories.dialogs.CuackDialog
import com.mmfsin.sabelotodo.presentation.dashboard.dialog.ExitDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mInterstitialAd: InterstitialAd? = null

    var fromRealm = false
    var inDashboard = false
    var firstTime = true

    var bsHeightSetted = false
    var bsCollapsedHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(100)
        setTheme(R.style.Theme_Sabelotodo)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBar()
        setAds()
        setToolbarListeners()
    }

    private fun changeStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
    }

    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        showBanner(visible = false)
        loadInterstitial(AdRequest.Builder().build())
    }

    private fun setToolbarListeners() {
        binding.apply {
            ivDuck.setOnClickListener {
                ivDuck.isEnabled = false
                CuackDialog().show(supportFragmentManager, "")
                object : CountDownTimer(2000, 100) {
                    override fun onTick(p0: Long) {}
                    override fun onFinish() {
                        ivDuck.isEnabled = true
                    }
                }.start()
            }

            ivBack.setOnClickListener {
                inDashboard = false
                onBack()
            }
        }
    }

    fun toolbarIcon(showDuck: Boolean) {
        binding.apply {
            ivDuck.isVisible = showDuck
            ivBack.isVisible = !showDuck
        }
    }

    fun toolbarVisibility(visible: Boolean) {
        binding.appBar.isVisible = visible
    }

    fun toolbarText(title: String) {
        binding.tvTitle.text = title
    }

    fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(this,
            getString(R.string.interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    loadInterstitial(AdRequest.Builder().build())
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }

    fun showInterstitial() {
        mInterstitialAd?.let {
            it.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }

    fun showBanner(visible: Boolean, bannerBgColor: Int? = null) {
        binding.apply {
            adView.isVisible = visible
            val background = bannerBgColor ?: R.color.light_grey
            clMain.background = ContextCompat.getDrawable(this@MainActivity, background)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dialog = ExitDialog() { onBack() }
        if (inDashboard) dialog.show(supportFragmentManager, "")
        else onBack()
    }

    private fun onBack() = onBackPressedDispatcher.onBackPressed()
}