package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.DialogUploadBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

private const val REQUEST_CODE_IMAGE_PICK = 0

class CustomUploadDialog : DialogFragment() {

    private lateinit var bind:DialogUploadBinding
    var currFile: Uri? = null
    private val imageRef = Firebase.storage.reference
    private val pathRef = Firebase.firestore.collection("paths")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater,R.layout.dialog_upload,container,false)

        bind.spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                changeSpinnerData()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }

        bind.ivPreview.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                //it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
                it.type = "image/*"
                startActivityForResult(it,REQUEST_CODE_IMAGE_PICK)
            }
        }
        bind.btnUpload.setOnClickListener{
            val path = "${bind.tvDialogCourse.text}/${bind.tvDialogBranch.text}/${bind.spinner3.selectedItem}/${bind.spSubjects.selectedItem}/${bind.etPaperTitile.text.trim()}/"
            uploadImageToStorage(path)
            val pat = "${bind.tvDialogCourse.text}/${bind.tvDialogBranch.text}/${bind.spinner3.selectedItem}/${bind.spSubjects.selectedItem}/"
            uploadPath(pat)
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

    private fun uploadImageToStorage(path:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            currFile?.let {
                if(bind.spSubjects.selectedItem.toString()!="Select Subject" && bind.spinner3.selectedItem.toString()!="Select Year" && bind.etPaperTitile.text.isNotEmpty()){
                    imageRef.child("${path}${it.hashCode()}").putFile(it).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,"Successfully Uploaded", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,"Please Enter all details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun uploadPath(path:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            pathRef.document().set(Path(path,"${bind.etPaperTitile.text.trim()}")).await()
        }catch (e:Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun changeSpinnerData() {
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