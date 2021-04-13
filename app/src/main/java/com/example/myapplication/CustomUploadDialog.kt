package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.DialogUploadBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.ArrayList
import kotlin.math.sign

private const val REQUEST_CODE_IMAGE_PICK = 0

class CustomUploadDialog : DialogFragment() {

    private lateinit var bind:DialogUploadBinding
    var currFile: Uri? = null
    private val imageRef = Firebase.storage.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater,R.layout.dialog_upload,container,false)

        bind.spinner3.onItemSelectedListener


        bind.ivPreview.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                //it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
                it.type = "image/*"
                startActivityForResult(it,REQUEST_CODE_IMAGE_PICK)
            }
        }
        bind.btnUpload.setOnClickListener{
            uploadImageToStorage()
        }
        val bundle = arguments

        if(bundle?.getString("TEXT1","")== "pdf"){
            bind.ivPreview.setImageResource(R.drawable.ic_pdficon)
        }
        bind.tvDialogBranch.text = bundle?.getString("TEXT","")
        return bind.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode== REQUEST_CODE_IMAGE_PICK){
            data?.data?.let {
                currFile = it
                bind.ivPreview.setImageURI(it)
            }
        }
    }

    private fun uploadImageToStorage() = CoroutineScope(Dispatchers.IO).launch {
        try {
            currFile?.let {
                if(bind.spSubjects.selectedItem!="Select Subject" && bind.spinner3.selectedItem!="Select Year"){
                    imageRef.child("${bind.tvDialogCourse.text}/${bind.tvDialogBranch.text}/${bind.spinner3.selectedItem}/${bind.spSubjects.selectedItem}/${it.hashCode()}").putFile(it).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,"Successfully Uploaded", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,"Please Enter Subject", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun changeSpinnerData(){
        val a  = ArrayList<String>()
        when(bind.spinner3.selectedItem.toString()){
            "Select Year" -> {
                a.add("Select Subject")
            }
            "First Year" -> {
                a.addAll(resources.getStringArray(R.array.subYear1))
            }
            "Second Year" -> {
                a.addAll(resources.getStringArray(R.array.subYear2))
            }
            "Third Year" -> {
                a.addAll(resources.getStringArray(R.array.subYear3))
            }
        }
        bind.subList = a
    }


}