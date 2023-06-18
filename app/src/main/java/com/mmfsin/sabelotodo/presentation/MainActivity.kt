package com.mmfsin.sabelotodo.presentation

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.sabelotodo.R
import com.mmfsin.sabelotodo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(500)
        setTheme(R.style.Theme_Sabelotodo)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAds()
        setToolbarListeners()
    }

    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        showBanner(visible = false)
        loadInterstitial(AdRequest.Builder().build())
    }

    private fun setToolbarListeners() {
        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    fun toolbarIcon(showDuck: Boolean) {
        binding.apply {
            ivDuck.isVisible = showDuck
            ivBack.isVisible = !showDuck
        }
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
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }

    fun showBanner(visible: Boolean) {
        binding.adView.isVisible = visible
    }
}