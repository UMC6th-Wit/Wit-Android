package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.data.remote.search.Souvenir
import com.umc.umc_6th_wit_android.databinding.ActivityCustomBinding

class CustomActivity : AppCompatActivity() {
    lateinit var binding: ActivityCustomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBinding.inflate(layoutInflater)

        binding.cancelBtn.setOnClickListener {
            finish()
        }
// ArrayList 초기화
        var items: ArrayList<Souvenir> = ArrayList()

        // 임의 test data 추가
        items.add(Souvenir(1,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f, 0, 1))
        items.add(Souvenir(2,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1 ))
        items.add(Souvenir(3,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1))
        items.add(Souvenir(4,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1))
        items.add(Souvenir(5,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1))
        items.add(Souvenir(6,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1))
        items.add(Souvenir(7,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1))
        items.add(Souvenir(8,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f,0, 1))


        val adapter = CustomRVAdapter(items)
        adapter.setOnItemClickListener(object : CustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@CustomActivity, ProductDetailFragment::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })

        binding.customRv.adapter = adapter
        binding.customRv.layoutManager  = GridLayoutManager(this@CustomActivity, 2)

        setContentView(binding.root)
    }
}