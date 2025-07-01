package com.example.seacatering.ui.user.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityCheckoutBinding
import com.example.seacatering.model.DataCheckout
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.ui.user.subcription.SubscriptionViewModel
import com.example.seacatering.utils.FormatRupiah.formatRupiah
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private val subscriptionViewModel: SubscriptionViewModel by viewModels()

    private var dataCheckout: DataSubscription? = null

    private lateinit var paymentsClient: PaymentsClient

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.checkout)

        dataCheckout = intent.getParcelableExtra("checkout_data")

        setupUI()
        setupCheckoutAction()
        cancelCheckout()
        subscriptionViewModel.checkUserCanSubscribe()
        paymentsClient = createPaymentsClient(this)
    }

    private fun setupUI() {
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

    private fun cancelCheckout() {
        binding.btnCancel.setOnClickListener {
            Toast.makeText(this, "Checkout cancelled", Toast.LENGTH_SHORT).show()
            finish()
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

                val totalInIdr = data.total_price

                val paymentRequestJson = createPaymentDataRequest(totalInIdr.toString())
                val request = PaymentDataRequest.fromJson(paymentRequestJson.toString())

                AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    this@CheckoutActivity,
                    LOAD_PAYMENT_DATA_REQUEST_CODE
                )
            }
        }
    }



    private fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    private fun createPaymentDataRequest(totalPrice: String): JSONObject {
        val transactionInfo = JSONObject().apply {
            put("totalPrice", totalPrice)
            put("totalPriceStatus", "FINAL")
            put("currencyCode", "IDR")
        }

        val tokenizationSpecification = JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", JSONObject().apply {
                put("gateway", "example")
                put("gatewayMerchantId", "exampleGatewayMerchantId")
            })
        }

        val cardPaymentMethod = JSONObject().apply {
            put("type", "CARD")
            put("parameters", JSONObject().apply {
                put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
                put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
                put("billingAddressRequired", true)
                put("billingAddressParameters", JSONObject().apply {
                    put("format", "FULL")
                })
            })
            put("tokenizationSpecification", tokenizationSpecification)
        }

        val merchantInfo = JSONObject().apply {
            put("merchantName", "SEA Catering")
        }

        return JSONObject().apply {
            put("apiVersion", 2)
            put("apiVersionMinor", 0)
            put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
            put("transactionInfo", transactionInfo)
            put("merchantInfo", merchantInfo)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        data?.let {
                            val paymentData = PaymentData.getFromIntent(it)
                            handlePaymentSuccess(paymentData)
                        }
                    }
                    RESULT_CANCELED -> {
                        Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show()
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Toast.makeText(this, "Payment failed: ${status?.statusMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData?) {
        val data = dataCheckout ?: return

        lifecycleScope.launch {
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
                "Payment success and subscription created!",
                Toast.LENGTH_LONG
            ).show()
            finish()
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

