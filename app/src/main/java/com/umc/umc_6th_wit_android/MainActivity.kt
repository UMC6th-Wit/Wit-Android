package com.umc.umc_6th_wit_android
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.umc.umc_6th_wit_android.home.HomeFragment
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityMainBinding
import com.umc.umc_6th_wit_android.list.ListFragment
import com.umc.umc_6th_wit_android.mypage.MypageFragment
import com.umc.umc_6th_wit_android.search.SearchFragment
import com.umc.umc_6th_wit_android.search.SearchMainFragment
import com.umc.umc_6th_wit_android.wish.WishFragment
import com.umc.umc_6th_wit_android.wish.WishListFragment

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

        // Intent를 통해 HomeFragment로 이동해야 하는지 확인
        if (intent.getBooleanExtra("navigateToHome", false)) {
            selectHomeFragment()  // HomeFragment로 이동
        }

    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setSelectedItemId(R.id.homeFragment)

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.listFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ListFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.wishFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, WishFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.myFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        supportActionBar?.hide()
    }

    fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        binding.mainBnv.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.mainCard.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun changeWishListFragment(boardTitle: String) {
        val fragment = WishListFragment()
        val bundle = Bundle()
        bundle.putString("boardTitle", boardTitle)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.main_frm, fragment)
            .addToBackStack("WishFragment")
            .commitAllowingStateLoss()
    }

    fun selectHomeFragment() {
        // 바텀 네비게이션의 선택된 항목을 homeFragment로 변경
        binding.mainBnv.selectedItemId = R.id.homeFragment

        // 홈 프래그먼트로 전환
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Intent에서 navigateToHome 플래그가 설정되어 있으면 HomeFragment로 전환
        intent?.let {
            if (it.getBooleanExtra("navigateToHome", false)) {
                selectHomeFragment()  // HomeFragment로 이동
            }
        }
    }
}
