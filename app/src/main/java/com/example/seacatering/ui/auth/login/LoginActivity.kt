package com.example.seacatering.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityLoginBinding
import com.example.seacatering.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val fullText = "Don’t have an account? Sign Up"
        val signUpText = "Sign Up"

        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.bgColor = android.graphics.Color.TRANSPARENT
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.green)
            }
        }

        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvSignUp.text = spannable
        binding.tvSignUp.movementMethod = LinkMovementMethod.getInstance()
        binding.tvSignUp.highlightColor = android.graphics.Color.TRANSPARENT

    }
}