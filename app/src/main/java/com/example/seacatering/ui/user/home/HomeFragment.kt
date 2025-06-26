package com.example.seacatering.ui.user.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.seacatering.R
import com.example.seacatering.databinding.FragmentHomeBinding
import com.example.seacatering.ui.user.menu.DetailMenuActivity
import com.example.seacatering.ui.user.menu.MealPlanAdapter
import com.example.seacatering.ui.user.menu.MenuViewModel
import com.example.seacatering.ui.user.review.ReviewActivity
import com.example.seacatering.ui.user.review.ReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val menuviewModel: MenuViewModel by viewModels()
    private val advantagesViewModel: HomeViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toReview()
        toMenu()
        showAdvantages()
        showBanner()
        showMenu()
        showTestimonial()
    }

    override fun onResume() {
        super.onResume()
        reviewViewModel.getAllTestimonial()
    }

    private fun showTestimonial() {
        val adapter = ReviewAdapter()
        binding.rvTestimonials.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTestimonials.adapter = adapter

        reviewViewModel.getReview.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    private fun showBanner() {
        val imageUrl = getString(R.string.banner_image_home)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(binding.imgDietPlan)
    }

    private fun showAdvantages() {
        val adapter = AdvantagesAdapter()
        binding.rvAdvantages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAdvantages.adapter = adapter

        lifecycleScope.launchWhenStarted {
            advantagesViewModel.advantages.collect { list ->
                adapter.submitList(list)
            }
        }
    }

    private fun showMenu() {
        val adapter = MealPlanAdapter { selectedMeal ->
            val intent = Intent(requireContext(), DetailMenuActivity::class.java)
            intent.putExtra("meal_plan", selectedMeal)
            startActivity(intent)
        }
        binding.rvPopularPlans.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularPlans.adapter = adapter

        lifecycleScope.launchWhenStarted {
            menuviewModel.mealPlans.collect { list ->
                adapter.submitList(list)
            }
        }

        lifecycleScope.launchWhenStarted {
            menuviewModel.errorMessage.collect { error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    Log.d("Menu Plan", error)
                }
            }
        }
    }

    private fun toMenu() {
        binding.btnToMenu.setOnClickListener {
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            navView.selectedItemId = R.id.navigation_menu
        }
    }

    private fun toReview() {
        binding.btnToReview.setOnClickListener {
            val intent = Intent(requireContext(), ReviewActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
