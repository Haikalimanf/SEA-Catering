package com.example.seacatering.ui.opening.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seacatering.R
import com.example.seacatering.databinding.FragmentThirdScreenBinding
import com.example.seacatering.ui.MainActivity
import com.example.seacatering.ui.auth.login.LoginActivity


class ThirdScreen : Fragment() {

    private lateinit var binding : FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThirdScreenBinding.inflate(layoutInflater)


        binding.btnFinish.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

}