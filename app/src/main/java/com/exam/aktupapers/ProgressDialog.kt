package com.exam.aktupapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.exam.aktupapers.databinding.DialogProgressBinding
import com.exam.aktupapers.databinding.DialogUploadBinding

class ProgressDialog :DialogFragment() {

    private var _binding: DialogProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogProgressBinding.inflate(inflater,container,false)
        return binding.root
    }
}