package com.example.fitpho.Guide

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.example.fitpho.Database.AppDatabase
import com.example.fitpho.Database.GuideEntity
import com.example.fitpho.Database.ViewModel
import com.example.fitpho.databinding.FragmentGuideBinding



class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!
    val List = mutableListOf<GuideEntity>()

    //GuideAdapter 생성
    private val guideAdapter by lazy { GuideAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewmodel = ViewModelProvider(this, ViewModel.Factory(requireActivity().application)).get(ViewModel::class.java)

        binding.exList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.exList.adapter = guideAdapter
        viewmodel.readAllData.observe(requireActivity(), Observer {
            guideAdapter.setData(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}