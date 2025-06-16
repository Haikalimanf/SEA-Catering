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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityLoginBinding
import com.example.seacatering.ui.MainActivity
import com.example.seacatering.ui.auth.register.RegisterActivity
import com.example.seacatering.utils.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupClickableSignUp()
        observeLoginState()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnLogin.isEnabled = false
                    }
                    is AuthState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    AuthState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                    }
                }
            }
        }
    }


    private fun setupClickableSignUp() {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}