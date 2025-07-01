package com.example.seacatering.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityLoginBinding
import com.example.seacatering.model.enums.UserRole
import com.example.seacatering.model.state.AuthState
import com.example.seacatering.model.state.RoleResultState
import com.example.seacatering.ui.MainActivity
import com.example.seacatering.ui.admin.dashboard.DashboardAdminActivity
import com.example.seacatering.ui.auth.register.RegisterActivity
import com.example.seacatering.utils.Validator.isValidPassword
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
        observeRoleState()
        btnLogin()
        btnLoginWithGoogle()
    }

    private fun btnLoginWithGoogle() {
        binding.btnLoginWithGoogle.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            viewModel.signInWithGoogle(this)
        }
    }

    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else if (email.isEmpty()) {
                binding.edtEmail.error = "Please enter your email"
            } else if (password.isEmpty()) {
                binding.passwordError.text = getString(R.string.note_fields_password_isEmpty)
                binding.passwordError.visibility = View.VISIBLE
            }
        }
    }

    private fun observeRoleState() {
        lifecycleScope.launchWhenStarted {
            viewModel.roleState.collect { state ->
                when (state) {
                    is RoleResultState.Loading -> {
                    }
                    is RoleResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true

                        val intent = when (state.role) {
                            UserRole.ADMIN -> Intent(this@LoginActivity, DashboardAdminActivity::class.java)
                            UserRole.USER -> Intent(this@LoginActivity, MainActivity::class.java)
                        }

                        startActivity(intent)
                        finish()
                    }
                    is RoleResultState.NotLoggedIn,
                    is RoleResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this@LoginActivity, "Failed to determine user role", Toast.LENGTH_SHORT).show()
                    }
                }
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
                        viewModel.checkUserRole()
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