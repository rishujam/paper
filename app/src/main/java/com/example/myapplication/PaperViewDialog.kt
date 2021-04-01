package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogPaperViewBinding.inflate(inflater,container,false)
        val bundle = arguments
        val paperName =bundle?.getString("TEXTT")
        val subSelected = bundle?.getString("TEXTT3")
        val yearSelected = bundle?.getString("TEXTT2")
        val branchSelected = bundle?.getString("TEXTT1")
        showImage("B.Tech/${branchSelected}/${yearSelected}/${subSelected}/${paperName}")
        return binding.root
    }



    private fun showImage(path:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val max  = 5L * 1024 * 1024
            val dialog = ProgressDialog()
            withContext(Dispatchers.Main){
                dialog.show(childFragmentManager,"progress1")
            }
            val byte = imageRef.child(path).getBytes(max).await()
            val bp = BitmapFactory.decodeByteArray(byte,0,byte.size)
            withContext(Dispatchers.Main){
                dialog.dismiss()
                binding.image.setImageBitmap(bp)
            }

        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}