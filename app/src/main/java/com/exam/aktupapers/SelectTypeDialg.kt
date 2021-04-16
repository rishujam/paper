package com.exam.aktupapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.exam.aktupapers.databinding.DialogSelectBinding
import com.exam.aktupapers.databinding.DialogUploadBinding

class SelectTypeDialog : DialogFragment() {

    private var _binding: DialogSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogSelectBinding.inflate(inflater, container, false)
        binding.btnAsImage.setOnClickListener {
            val dialog = CustomUploadDialog()
            val bundle=arguments
            val string = bundle?.getString("TEXT","")

            val bun = Bundle()
            bun.putString("TEXT",string)
            bun.putString("TEXT1","image")
            dialog.arguments = bun
            dialog.show(childFragmentManager,"customDialog")
        }
        binding.btnAsPdf.setOnClickListener {
            val dialog = CustomUploadDialog()
            val bundle = arguments
            val string = bundle?.getString("TEXT","")

            val bun = Bundle()
            bun.putString("TEXT",string)
            bun.putString("TEXT1","pdf")
            dialog.arguments = bun
            dialog.show(childFragmentManager,"customDialog")
        }
        return binding.root
    }
}