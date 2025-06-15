package com.example.seacatering.ui.user.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.seacatering.R
import com.example.seacatering.databinding.FragmentHomeBinding
import com.example.seacatering.ui.user.review.ReviewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnToReview.setOnClickListener {
            val intent = Intent(requireContext(), ReviewActivity::class.java)
            startActivity(intent)
        }

        binding.btnToMenu.setOnClickListener {
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            navView.selectedItemId = R.id.navigation_menu
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}