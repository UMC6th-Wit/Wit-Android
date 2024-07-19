package com.umc.umc_6th_wit_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowInsetsControllerCompat
import com.umc.umc_6th_wit_android.databinding.ActivityMainBinding
import com.umc.umc_6th_wit_android.home.HomeFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.window.apply {
            statusBarColor = resources.getColor(R.color.color_translate, null)
        }

        initBottomNavigation()

    }
    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

       /* binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.home -> {
                    item.setIcon(R.drawable.ic_bottom_home_select) // 선택한 이미지 변경
                    binding.mainBnv.menu.findItem(R.id.list).setIcon(R.drawable.ic_bottom_list_no_select)
                    binding.mainBnv.menu.findItem(R.id.search).setIcon(R.drawable.ic_bottom_search_no_select)
                    binding.mainBnv.menu.findItem(R.id.wish).setIcon(R.drawable.ic_bottom_wish_no_select)
                    binding.mainBnv.menu.findItem(R.id.my).setIcon(R.drawable.ic_bottom_my_no_select)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }*/
    }
}