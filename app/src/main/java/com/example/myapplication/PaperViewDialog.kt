package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.DialogPaperViewBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PaperViewDialog :DialogFragment() {

    private var _binding: DialogPaperViewBinding? = null
    private val binding get() = _binding!!
    private val imageRef = Firebase.storage.reference


    //// BUG : GAP IN IMAGES //////


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogPaperViewBinding.inflate(inflater,container,false)
        val bundle = arguments
        val paperName =bundle?.getString("TEXTT")
        val subSelected = bundle?.getString("TEXTT3")
        val yearSelected = bundle?.getString("TEXTT2")
        val branchSelected = bundle?.getString("TEXTT1")
        showImage("B.Tech/${branchSelected}/${yearSelected}/${subSelected}/${paperName}/")
        return binding.root
    }



    private fun showImage(path:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val images = imageRef.child(path).listAll().await()
            val imageMaps = mutableListOf<Bitmap>()
            val dialog = ProgressDialog()
            withContext(Dispatchers.Main){
                dialog.show(childFragmentManager,"processing")
            }
            for(image in images.items){
                val max = 7L * 1024 *1024
                val byte = image.getBytes(max).await()
                val bp = BitmapFactory.decodeByteArray(byte,0,byte.size)
                imageMaps.add(bp)
            }
            withContext(Dispatchers.Main){
                dialog.dismiss()
                val imageAdapter  = ImagesAdapter(imageMaps)
                binding.rvImage.apply {
                    adapter = imageAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}