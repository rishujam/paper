package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.DialogUploadBinding
import com.example.myapplication.databinding.FragmentCollegeBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val REQUEST_CODE_IMAGE_PICK = 0

class CustomUploadDialog : DialogFragment() {

    private var _binding: DialogUploadBinding? = null
    private val binding get() = _binding!!
    var currFile: Uri? = null
    private val imageRef = Firebase.storage.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogUploadBinding.inflate(inflater, container, false)
        binding.ivPreview.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it,REQUEST_CODE_IMAGE_PICK)
            }
        }
        binding.btnUpload.setOnClickListener{
            uploadImageToStorage("myImage")
        }
        val bundle = arguments

        binding.tvDialogBranch.text = bundle?.getString("TEXT","")
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode== REQUEST_CODE_IMAGE_PICK){
            data?.data?.let {
                currFile = it
                binding.ivPreview.setImageURI(it)
            }
        }
    }

    private fun uploadImageToStorage(filename:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            currFile?.let {
                if(binding.etSubject.text!=null){
                    imageRef.child("${binding.tvDialogCourse.text}/${binding.tvDialogBranch.text}/${binding.spinner3.selectedItem}/${binding.etSubject.text}/${it.hashCode()}").putFile(it).await()
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,"Please Enter Subject", Toast.LENGTH_SHORT).show()
                    }
                }
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Successfully Uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}