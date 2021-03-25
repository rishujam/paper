
package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentCollegeBinding


class CollegeFragment : Fragment() {

    private var _binding: FragmentCollegeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollegeBinding.inflate(inflater, container, false)
        binding.actionBarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(context, parent?.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show()
            }
        }
        binding.btnUploadFile.setOnClickListener{
            var dialog = CustomUploadDialog()
            dialog.show(childFragmentManager,"customDialog")
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}