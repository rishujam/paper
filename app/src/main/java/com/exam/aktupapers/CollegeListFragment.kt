package com.exam.aktupapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.aktupapers.databinding.FragmentCollegeListBinding

class CollegeListFragment : Fragment(), CollegeListAdapter.OnItemClickListener {

    private var _binding: FragmentCollegeListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollegeListBinding.inflate(inflater, container, false)
        var colleges = mutableListOf(College("Galgotia","Uttar Pradesh", "AKTU"),
            College("GL","UP","AKTU" )
        )

        var adapter = CollegeListAdapter(colleges,this)
        binding.apply {
            rvCollegeList.adapter = adapter
            rvCollegeList.layoutManager = LinearLayoutManager(context)
            flabAdd.setOnClickListener {
                
            }
        }
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        Navigation.findNavController(binding.root).navigate(R.id.action_collegeListFragment_to_collegeFragment)
    }

}