package com.example.seacatering.ui.opening

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.seacatering.R
import com.example.seacatering.model.enums.UserRole
import com.example.seacatering.model.local.OnboardingPreferences
import com.example.seacatering.model.state.RoleResultState
import com.example.seacatering.ui.MainActivity
import com.example.seacatering.ui.admin.dashboard.DashboardAdminActivity
import com.example.seacatering.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)

            val isOnboardingDone = OnboardingPreferences.isOnboardingCompleted(requireContext())
            if (!isOnboardingDone) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
                return@launch
            }

            viewModel.checkUserRole()
        }

        observeRoleState()
    }

    private fun observeRoleState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.roleState.collect { state ->
                when (state) {
                    is RoleResultState.Loading -> {  }
                    is RoleResultState.NotLoggedIn -> {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }
                    is RoleResultState.Success -> {
                        val intent = when (state.role) {
                            UserRole.ADMIN -> Intent(requireContext(), DashboardAdminActivity::class.java)
                            UserRole.USER -> Intent(requireContext(), MainActivity::class.java)
                        }
                        startActivity(intent)
                        activity?.finish()
                    }
                    is RoleResultState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }
                }
            }
        }
    }
}