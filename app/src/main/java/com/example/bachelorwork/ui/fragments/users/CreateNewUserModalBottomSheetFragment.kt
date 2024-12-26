package com.example.bachelorwork.ui.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bachelorwork.databinding.FragmentModalBottomSheetCreateNewUserBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateNewUserModalBottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentModalBottomSheetCreateNewUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModalBottomSheetCreateNewUserBinding.inflate(inflater, container, false)
        return binding.root
    }
}