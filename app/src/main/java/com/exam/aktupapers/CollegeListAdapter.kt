package com.exam.aktupapers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exam.aktupapers.databinding.CollegeItemBinding

class CollegeListAdapter(
    var colleges:List<College>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<CollegeListAdapter.CollegeViewHolder>() {

    inner class CollegeViewHolder(val binding: CollegeItemBinding):RecyclerView.ViewHolder(binding.root), View.OnClickListener{
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollegeViewHolder {
        return CollegeViewHolder(CollegeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return colleges.size
    }

    override fun onBindViewHolder(holder: CollegeViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = colleges[position].name
            tvState.text = colleges[position].state
            tvUniversity.text = colleges[position].university
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}