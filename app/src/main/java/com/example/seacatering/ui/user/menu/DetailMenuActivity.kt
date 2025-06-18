package com.example.seacatering.ui.user.menu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityDetailMenuBinding
import com.example.seacatering.model.DataMealPlan
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMenuActivity : AppCompatActivity() {

    private var _binding: ActivityDetailMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.detail_menu)
        supportActionBar?.title = pageTitle

        val mealPlan = intent.getParcelableExtra<DataMealPlan>("meal_plan")

        showMealPlanData(mealPlan)

    }

    private fun showMealPlanData(mealPlan: DataMealPlan?) {
        binding.apply {
            Glide.with(this@DetailMenuActivity)
                .load(mealPlan?.imageUri)
                .into(imgDietPlan)

            nameMenu.text = mealPlan?.name
            descMenu.text = mealPlan?.longDescription?.replace("\\n", "\n")

            tvDescYouWillGet.text = mealPlan?.benefits?.joinToString(separator = "\n• ", prefix = "• ")

            tvDescMenuOnWeekly.text = mealPlan?.weeklyMenu?.joinToString(separator = "\n• ", prefix = "• ")

            tvDescNutrional.text = mealPlan?.nutritionalInfo?.joinToString(separator = "\n")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}