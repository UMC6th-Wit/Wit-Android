package com.umc.umc_6th_wit_android.onboarding

import android.os.Bundle
import android.view.View
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

        // 초기에는 backButton을 숨김
        binding.onboardingBack.visibility = View.GONE

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 1페이지에서는 backButton 숨김, 그 외 페이지에서는 표시
                binding.onboardingBack.visibility = if (position > 0) View.VISIBLE else View.GONE
            }
        })

        binding.onboardingBack.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.currentItem = currentItem - 1
            }
        }

    }

    fun goToNextPage() {
        val nextItem = viewPager.currentItem + 1
        if (nextItem < viewPager.adapter?.itemCount ?: 0) {
            viewPager.setCurrentItem(nextItem, true)
        }
    }


}