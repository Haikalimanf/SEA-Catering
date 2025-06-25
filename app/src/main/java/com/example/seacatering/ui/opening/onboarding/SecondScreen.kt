package com.example.seacatering.ui.opening.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.seacatering.R
import com.example.seacatering.databinding.FragmentSecondScreenBinding


class SecondScreen : Fragment() {

    private lateinit var binding : FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSecondScreenBinding.inflate(layoutInflater)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.btnNext.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }

}