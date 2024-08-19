package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentReviewMinBinding

class ReviewMinFragment : Fragment() {

    private var _binding: FragmentReviewMinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewMinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToReviewBtnIv.setOnClickListener {
            val intent = Intent(requireContext(), ReviewOnlyActivity::class.java)
            startActivity(intent)
        }

        binding.moreReviewBtnIv.setOnClickListener{
            val intent = Intent(requireContext(), ReviewOnlyActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val adapter = ReviewMinAdapter(itemList)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
