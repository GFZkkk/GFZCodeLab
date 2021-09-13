package com.gfz.lab.ui.fragment

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gfz.lab.databinding.FragmentTabMainBinding
import com.gfz.lab.ext.TOP
import com.gfz.lab.ui.base.BaseVBFragment
import com.gfz.lab.utils.TopLog

class TabMainFragment : BaseVBFragment<FragmentTabMainBinding>() {

    override fun initView() {
        val nav: NavController = Navigation.findNavController(binding.fcvTab)
    }
}