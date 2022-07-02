package com.mmfsin.sabelotodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.sabelotodo.databinding.ActivityMainBinding
import com.mmfsin.sabelotodo.presentation.dashboard.Dashboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardOne.item.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Dashboard())
                .commit()
        }
    }
}