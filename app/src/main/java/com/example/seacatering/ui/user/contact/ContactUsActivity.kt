package com.example.seacatering.ui.user.contact

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity() {

    private var _binding: ActivityContactUsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.contact_us)
        supportActionBar?.title = pageTitle

        contactTheNumber()
    }

    private fun contactTheNumber() {
        binding.btnToCall.setOnClickListener {
            val mobileNumber = getString(R.string.number_brian).replace("+", "").replace(" ", "")
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + mobileNumber)
            startActivity(dialIntent)
        }

        binding.btnToWhatsApp.setOnClickListener {
            val mobileNumber = getString(R.string.number_brian).replace("+", "").replace(" ", "")
            val message = getString(R.string.message)
            val url = "https://api.whatsapp.com/send?phone=$mobileNumber&text=${Uri.encode(message)}"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
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