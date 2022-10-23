package com.example.fitpho

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitpho.databinding.DialogDatetimePickerBinding
import com.example.fitpho.databinding.FragmentScheduleAddBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat

class ScheduleAdd : Fragment(), TabLayout.OnTabSelectedListener {

    private var _binding: FragmentScheduleAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: DialogDatetimePickerBinding


    private var mFormatter: SimpleDateFormat = SimpleDateFormat("MMMM dd yyyy hh:mm aa");
    //private var listener = SlideDateTimeListener()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView3.setOnClickListener{
            dialogTime(0)
        }
        binding.btnCancel.setOnClickListener{
            findNavController().navigate(R.id.calenderFragment)
        }
    }

    private fun dialogTime(type: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        view = DialogDatetimePickerBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        view.frame.currentItem = tab!!.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }


}