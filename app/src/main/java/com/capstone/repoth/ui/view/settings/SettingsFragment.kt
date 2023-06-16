package com.capstone.repoth.ui.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.repoth.R
import com.capstone.repoth.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}