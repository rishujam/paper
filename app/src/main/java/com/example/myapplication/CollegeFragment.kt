
package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentCollegeBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class CollegeFragment : Fragment(), CollegeListAdapter.OnItemClickListener {

    override fun onItemClick(position: Int) {
        when (binding.tvFolderType.text) {
            "Papers" -> {
                val paperName = adapter.papers[position].name

            }
            resources.getStringArray(R.array.subYear1)[0] -> {
                readFiles("B.Tech/CS-IT/First Year/Engine. Physics/")
                binding.tvFolderType.text = "Papers"

            }
            "Select Year" -> {
                binding.apply {
                    adapter = PaperListAdapter(sub1,this@CollegeFragment)
                    rvPapers.adapter = adapter
                    rvPapers.layoutManager = LinearLayoutManager(context)
                    tvFolderType.text= resources.getStringArray(R.array.subYear1)[0]
                }
            }
        }
    }

    private var _binding: FragmentCollegeBinding? = null
    private val binding get() = _binding!!
    private val imageRef = Firebase.storage.reference
    private var sub1  = mutableListOf<Paper>()
    private lateinit var adapter :PaperListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollegeBinding.inflate(inflater, container, false)

        val years = listOf(Paper("First Year"), Paper("Second Year"), Paper("Third Year"), Paper("Fourth Year"))
        adapter  = PaperListAdapter(years,this)
        binding.rvPapers.adapter = adapter
        binding.rvPapers.layoutManager = LinearLayoutManager(context)

        for(i in 1 until resources.getStringArray(R.array.subYear1).size){
            val sub = resources.getStringArray(R.array.subYear1)[i]
            sub1.add(Paper(sub))
        }

        binding.actionBarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }
        binding.btnUploadFile.setOnClickListener{
            var dialog = SelectTypeDialog()
            val bundle=Bundle()
            bundle.putString("TEXT",binding.spinner.selectedItem.toString())
            dialog.arguments = bundle
            dialog.show(childFragmentManager,"selectDialog")
        }
        return binding.root
    }

    private fun downloadFile(path:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = imageRef.child(path).getBytes(maxDownloadSize).await()
            val bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.size)

        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun readFiles(path:String){
        val imageUrls = mutableListOf<Paper>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dialog = ProgressDialog()
                withContext(Dispatchers.Main){
                    dialog.show(childFragmentManager,"progress")
                }
                val images = imageRef.child(path).listAll().await()
                for(image in images.items){
                    val url = image.name
                    imageUrls.add(Paper(url))
                }
                withContext(Dispatchers.Main){
                    dialog.dismiss()
                }
                withContext(Dispatchers.Main){
                    adapter = PaperListAdapter(imageUrls, this@CollegeFragment)
                    binding.rvPapers.adapter = adapter
                    binding.rvPapers.layoutManager = LinearLayoutManager(context)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}