package com.example.seacatering.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityRegisterBinding
import com.example.seacatering.model.state.AuthState
import com.example.seacatering.ui.auth.login.LoginActivity
import com.example.seacatering.utils.Validator.isValidPassword
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
        btnSignUp()
        btnLoginWithGoogle()
    }

    private fun validateInputs(): Boolean {
        val usernameValid = isUsernameValid()
        val emailValid = isEmailValid()
        val passwordValid = isPasswordValid()

        return usernameValid && emailValid && passwordValid
    }


    private fun btnSignUp() {
        binding.btnSignUp.setOnClickListener {
            if (validateInputs()) {
                val username = binding.edtUsername.text.toString().trim()
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()

                viewModel.registerAndSaveUser(email, password, username)
            }
        }
    }

    private fun isUsernameValid(): Boolean {
        val username = binding.edtUsername.text.toString().trim()
        return if (username.isEmpty()) {
            binding.edtUsername.error = "Please enter your name"
            false
        } else true
    }

    private fun isEmailValid(): Boolean {
        val email = binding.edtEmail.text.toString().trim()
        return if (email.isEmpty()) {
            binding.edtEmail.error = "Please enter your email"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Invalid email format."
            false
        } else true
    }

    private fun isPasswordValid(): Boolean {
        val password = binding.edtPassword.text.toString().trim()
        return when {
            password.isEmpty() -> {
                binding.passwordError.text = getString(R.string.note_fields_password_isEmpty)
                binding.passwordError.visibility = View.VISIBLE
                false
            }
            !isValidPassword(password) -> {
                binding.passwordError.text = getString(R.string.note_fields_password)
                binding.passwordError.visibility = View.VISIBLE
                false
            }
            else -> {
                binding.passwordError.visibility = View.GONE
                true
            }
        }
    }

    private fun btnLoginWithGoogle() {
        binding.btnLoginWithGoogle.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            viewModel.signInWithGoogle(this)
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