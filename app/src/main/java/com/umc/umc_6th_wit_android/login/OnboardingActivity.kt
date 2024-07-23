package com.umc.umc_6th_wit_android.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityOnboardingBinding


class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: DotsIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewPager = findViewById(R.id.onboarding_viewpager)
        viewPager.setUserInputEnabled(false);

        dotsIndicator = findViewById(R.id.onboarding_indicator)

        val adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)

    }

    fun goToNextPage() {
        val nextItem = viewPager.currentItem + 1
        if (nextItem < viewPager.adapter?.itemCount ?: 0) {
            viewPager.setCurrentItem(nextItem, true)
        }
    }


}