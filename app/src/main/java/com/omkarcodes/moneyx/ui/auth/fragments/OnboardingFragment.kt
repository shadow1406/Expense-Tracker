package com.omkarcodes.moneyx.ui.auth.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.databinding.FragmentOnBoardingBinding
import com.omkarcodes.moneyx.ui.auth.adapters.OnboardingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_on_boarding){

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding: FragmentOnBoardingBinding
        get() = _binding!!
    @Inject
    lateinit var pref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnBoardingBinding.bind(view)

        binding.apply {

            vpOnboarding.adapter = OnboardingAdapter()
            dotIndicator.setViewPager2(vpOnboarding)

            btnSignUp.setOnClickListener {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToSignUpFragment())
            }

            btnLogin.setOnClickListener {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment())
            }

            lifecycleScope.launch {
                while (true){
                    delay(3000L)
                    if (vpOnboarding.currentItem == 2){
                        vpOnboarding.setCurrentItem(0,true)
                    }else vpOnboarding.setCurrentItem(vpOnboarding.currentItem +1, true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            if (pref.getString("pin","")!!.isNotEmpty())
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToPasswordFragment())
            else
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToPasswordFragment(isPinCreation = true))
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}