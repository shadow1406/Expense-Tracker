package com.omkarcodes.moneyx.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.databinding.BottomSheetLogOutBinding
import com.omkarcodes.moneyx.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogOutBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_log_out,container,false)
    }

    private val viewModel: AuthViewModel by viewModels()

    private var _binding: BottomSheetLogOutBinding? = null
    private val binding: BottomSheetLogOutBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetLogOutBinding.bind(view)

        binding.apply {

            btnNo.setOnClickListener {
                this@LogOutBottomSheet.dismiss()
            }

            btnYes.setOnClickListener {
                viewModel.logout()
                findNavController().navigate(LogOutBottomSheetDirections.actionLogOutBottomSheetToLoginFragment())
                this@LogOutBottomSheet.dismiss()
            }

        }


    }

}