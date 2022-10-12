package com.omkarcodes.moneyx.ui.home.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.databinding.FragmentMainBinding
import com.omkarcodes.moneyx.ui.home.HomeViewModel
import com.omkarcodes.moneyx.ui.home.adapters.MainViewpagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main){

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val toBottomRight: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_right_anim) }
    private val fromBottomRight: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_right_anim) }
    private val toBottomLeft: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_left_anim) }
    private val fromBottomLeft: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_left_anim) }

    private var clicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.apply {

            vpMain.adapter = MainViewpagerAdapter(requireActivity())

            vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0)
                        bottomNavigationView.selectedItemId = R.id.navHome
                    else
                        bottomNavigationView.selectedItemId = R.id.navTransactions
                }
            })

            bottomNavigationView.setOnNavigationItemSelectedListener {
                if (it.itemId == R.id.navHome){
                    vpMain.currentItem = 0
                }else{
                    vpMain.currentItem = 1
                }
                true
            }

            fabAdd.setOnClickListener {
                setVisibility(clicked)
                setAnimation(clicked)
                clicked = !clicked
            }

            fabIncome.setOnClickListener {
                clicked = !clicked
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToNewTransactionFragment(isIncome = true))
            }

            fabExpenses.setOnClickListener {
                clicked = !clicked
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToNewTransactionFragment(isIncome = false))
            }

            viewModel.seeAllClicked.observe(viewLifecycleOwner,{
                if (it){
                    vpMain.currentItem = 1
                    viewModel.seeAllClicked.postValue(false)
                }
            })

            viewModel.drawerOpen.observe(viewLifecycleOwner){
                if (it){
                    if (drawer.isOpen)
                        drawer.closeDrawer(GravityCompat.START)
                    else drawer.openDrawer(GravityCompat.START)
                }
            }

            drawerView.setNavigationItemSelectedListener {
                if (it.itemId == R.id.nav_home){
                    binding.vpMain.currentItem = 0
                    drawer.closeDrawer(GravityCompat.START)
                }else if(it.itemId == R.id.nav_transactions){
                    binding.vpMain.currentItem = 1
                    drawer.closeDrawer(GravityCompat.START)
                }else{
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToLogOutBottomSheet())
                }
                true
            }
            viewModel.getUsername()
            viewModel.userName.observe(viewLifecycleOwner) {
                binding.drawerView.getHeaderView(0).findViewById<TextView>(R.id.tvUsername).text = it
            }
        }

    }
    private fun setVisibility(clicked: Boolean) {
        if (!clicked){
            binding.fabIncome.visibility = View.VISIBLE
            binding.fabExpenses.visibility = View.VISIBLE
        }else{
            binding.fabIncome.visibility = View.INVISIBLE
            binding.fabExpenses.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked){
            binding.fabIncome.startAnimation(fromBottomRight)
            binding.fabExpenses.startAnimation(fromBottomLeft)
            binding.fabAdd.startAnimation(rotateOpen)
        }else{
            binding.fabIncome.startAnimation(toBottomRight)
            binding.fabExpenses.startAnimation(toBottomLeft)
            binding.fabAdd.startAnimation(rotateClose)
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}