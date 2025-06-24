package com.example.seacatering.ui.user.checkout

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityCheckoutBinding
import com.example.seacatering.model.DataCheckout
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.ui.user.menu.MenuViewModel
import com.example.seacatering.ui.user.subcription.SubscriptionViewModel
import com.example.seacatering.utils.FormatRupiah.formatRupiah
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private val subscriptionViewModel: SubscriptionViewModel by viewModels()

    private var dataCheckout: DataSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupUI()
        setupCheckoutAction()
        subscriptionViewModel.checkUserCanSubscribe()
    }


    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.checkout)

        dataCheckout = intent.getParcelableExtra("checkout_data")

        dataCheckout?.let {
            binding.name.text = it.username
            binding.noPhone.text = it.phone_number
            binding.planSelection.text = it.meal_plan.joinToString(", ")
            binding.mealType.text = it.plan_type_name
            binding.deliveryDays.text = it.delivery_days.joinToString(", ")
            binding.allergies.text = it.allergies
            binding.totalPrice.text = formatRupiah(it.total_price)
        }
    }

    private fun setupCheckoutAction() {
        binding.btnCheckout.setOnClickListener {
            val data = dataCheckout
            if (data == null) {
                Toast.makeText(this, "Invalid subscription data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val canSubscribe = subscriptionViewModel.canSubscribe.value

                if (!canSubscribe) {
                    Toast.makeText(
                        this@CheckoutActivity,
                        "You already have an active or paused subscription.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                subscriptionViewModel.addSubscription(data)

                val checkout = DataCheckout(
                    id = UUID.randomUUID().toString(),
                    userId = data.userId,
                    name = data.username,
                    phone_number = data.phone_number,
                    plan_id = data.plan_id,
                    plan_type_name = data.plan_type_name,
                    meal_plan = data.meal_plan,
                    delivery_days = data.delivery_days,
                    allergies = data.allergies,
                    price = data.total_price
                )

                checkoutViewModel.addCheckOut(checkout)

                Toast.makeText(
                    this@CheckoutActivity,
                    "Checkout and subscription successful!\nTotal: ${formatRupiah(data.total_price)}",
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
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
