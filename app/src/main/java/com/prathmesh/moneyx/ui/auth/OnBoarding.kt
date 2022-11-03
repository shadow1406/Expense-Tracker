package com.prathmesh.moneyx.ui.auth

import com.prathmesh.moneyx.R

sealed class OnBoarding(val src: Int, val title: String, val subtitle: String){
    object Control : OnBoarding(
        R.drawable.ic_onboarding_1,
        "Gain total control \nof your money",
        "Become your own money manager and make every cent count")
    object Know : OnBoarding(
        R.drawable.ic_onboarding_2,
        "Know where your \nmoney goes",
        "Track your transaction easily,\nwith categories and financial report ")
    object Plan : OnBoarding(
        R.drawable.ic_onboarding_3,
        "Planning ahead",
        "Setup your budget for each category\nso you in control")
}
