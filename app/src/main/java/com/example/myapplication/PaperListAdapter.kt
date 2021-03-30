package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PaperItemBinding

class PaperListAdapter(
        var papers: List<Paper>,
        private val listener:CollegeListAdapter.OnItemClickListener
) : RecyclerView.Adapter<PaperListAdapter.PaperViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaperViewHolder {
        return PaperViewHolder(PaperItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return papers.size
    }

    override fun onBindViewHolder(holder: PaperViewHolder, position: Int) {
        holder.binding.apply {
            tvSubjectFolder.text = papers[position].name
        }

    }

    inner class PaperViewHolder(val binding :PaperItemBinding):RecyclerView.ViewHolder(binding.root),View.OnClickListener{
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }
}