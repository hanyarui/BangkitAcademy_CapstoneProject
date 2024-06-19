package com.dicoding.capstone.ui.tabLayout.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.databinding.FragmentProfileBinding
import com.dicoding.capstone.data.theme.SettingPreference
import com.dicoding.capstone.util.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.dicoding.capstone.data.local.UserPreference
import com.dicoding.capstone.ui.MainActivity
import com.dicoding.capstone.ui.auth.AuthViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize dependencies
        val auth = FirebaseAuth.getInstance()
        val userPreference = UserPreference(requireContext())
        val settingPreference = SettingPreference(requireContext())

        // Initialize ViewModelFactory
        val factory = ViewModelFactory(auth, userPreference, settingPreference)
        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

//        setTheme()
        setupLogoutButton()

        return root
    }

//    private fun setTheme() {
//        profileViewModel.getTheme().observe(viewLifecycleOwner) { isDarkModeActive ->
//            if (isDarkModeActive) {
//                binding.switchTheme.text = "Dark Theme"
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                binding.switchTheme.text = "Light Theme"
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            binding.switchTheme.isChecked = isDarkModeActive
//        }
//
//        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
//            profileViewModel.saveTheme(isChecked)
//        }
//    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
