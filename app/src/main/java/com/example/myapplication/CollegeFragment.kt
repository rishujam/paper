
package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentCollegeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CollegeFragment : Fragment(), CollegeListAdapter.OnItemClickListener {

    override fun onItemClick(position: Int) {
        when (binding.tvFolderType.text) {
            "Papers" -> {
                val dialog = PaperViewDialog()
                val bundle = Bundle()
                val paperName = adapter.papers[position].name
                bundle.putString("TEXTT",paperName)
                bundle.putString("TEXTT1",binding.spinner.selectedItem.toString())
                bundle.putString("TEXTT2",yearSelected)
                bundle.putString("TEXTT3",subSelected)
                dialog.arguments = bundle
                dialog.show(childFragmentManager,"viewDialog")
            }
            resources.getStringArray(R.array.subYear1)[0] -> {
                subSelected = adapter.papers[position].name
                readFolders("B.Tech/${binding.spinner.selectedItem}/${yearSelected}/${subSelected}/")
                binding.tvFolderType.text = "Papers"

            }
            "Select Year" -> {
                binding.apply {
                    yearSelected = adapter.papers[position].name
                    when(yearSelected){
                        "First Year" -> {
                            for(i in 1 until resources.getStringArray(R.array.subYear1).size){
                                val sub = resources.getStringArray(R.array.subYear1)[i]
                                sub1.add(Paper(sub))
                            }
                            adapter = PaperListAdapter(sub1,this@CollegeFragment)
                        }
                        "Second Year" -> {
                            for(i in 1 until resources.getStringArray(R.array.subYear2).size){
                                val sub = resources.getStringArray(R.array.subYear2)[i]
                                sub2.add(Paper(sub))
                            }
                            adapter = PaperListAdapter(sub2,this@CollegeFragment)
                        }
                        "Third Year" -> {
                            for(i in 1 until resources.getStringArray(R.array.subYear3).size){
                                val sub = resources.getStringArray(R.array.subYear3)[i]
                                sub3.add(Paper(sub))
                            }
                            adapter = PaperListAdapter(sub3,this@CollegeFragment)
                        }
                    }
                    rvPapers.adapter = adapter
                    rvPapers.layoutManager = LinearLayoutManager(context)
                    tvFolderType.text= resources.getStringArray(R.array.subYear1)[0]
                }
            }
        }
    }

    private var _binding: FragmentCollegeBinding? = null
    private val binding get() = _binding!!
    private val pathRef = Firebase.firestore.collection("paths")
    private var sub1 = mutableListOf<Paper>()
    private var sub2 = mutableListOf<Paper>()
    private var sub3 = mutableListOf<Paper>()
    private lateinit var adapter :PaperListAdapter
    private lateinit var yearSelected:String
    private lateinit var subSelected:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollegeBinding.inflate(inflater, container, false)

        val years = listOf(Paper("First Year"), Paper("Second Year"), Paper("Third Year"))
        adapter  = PaperListAdapter(years,this)
        binding.rvPapers.adapter = adapter
        binding.rvPapers.layoutManager = LinearLayoutManager(context)



        binding.actionBarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }
        binding.btnUploadFile.setOnClickListener{
            val dialog = SelectTypeDialog()
            val bundle=Bundle()
            bundle.putString("TEXT",binding.spinner.selectedItem.toString())
            dialog.arguments = bundle
            dialog.show(childFragmentManager,"selectDialog")
        }
        return binding.root
    }

    private fun  readFolders(path:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val list = ArrayList<Paper>()
            val dialog = ProgressDialog()
            withContext(Dispatchers.Main){
                dialog.show(childFragmentManager,"progress")
            }
            val querySnapshot = pathRef.get().await()
            for(document in querySnapshot.documents){
                val obj = document.toObject<Path>()
                if(obj?.path==path && !list.contains(Paper(obj.value))){
                    list.add(Paper(obj.value))
                }
            }
            withContext(Dispatchers.Main){
                dialog.dismiss()
                adapter = PaperListAdapter(list,this@CollegeFragment)
                binding.rvPapers.adapter = adapter
                binding.rvPapers.layoutManager =LinearLayoutManager(context)
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}