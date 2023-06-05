package com.capstone.repoth.ui.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.repoth.databinding.ActivityHomeBinding
import java.util.Locale

class HomeActivity : Fragment() {
    private var _bind: ActivityHomeBinding? = null
    private val bind get() = _bind!!

    private val locale: String = Locale.getDefault().language

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = ActivityHomeBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

}