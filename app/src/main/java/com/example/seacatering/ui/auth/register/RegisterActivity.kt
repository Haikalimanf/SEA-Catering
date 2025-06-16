package com.example.seacatering.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.example.seacatering.databinding.ActivityLoginBinding
import com.example.seacatering.databinding.ActivityRegisterBinding
import com.example.seacatering.ui.MainActivity
import com.example.seacatering.ui.auth.login.LoginActivity
import com.example.seacatering.utils.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupClickableSignIn()
        observeRegisterState()

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.register(email, password)
            } else {
                Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun observeRegisterState() {
        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnSignUp.isEnabled = false
                    }
                    is AuthState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSignUp.isEnabled = true
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSignUp.isEnabled = true
                        Toast.makeText(this@RegisterActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    AuthState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSignUp.isEnabled = true
                    }
                }
            }
        }
    }

    private fun setupClickableSignIn() {
        val fullText = "Already Have An Account? Sign In"
        val signUpText = "Sign In"

        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.bgColor = android.graphics.Color.TRANSPARENT
                ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.green)
            }
        }

        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvSignIn.text = spannable
        binding.tvSignIn.movementMethod = LinkMovementMethod.getInstance()
        binding.tvSignIn.highlightColor = android.graphics.Color.TRANSPARENT
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}