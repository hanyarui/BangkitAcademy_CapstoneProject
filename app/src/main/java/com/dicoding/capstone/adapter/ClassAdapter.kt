package com.dicoding.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.R
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dicoding.capstone.data.ClassDetails
import com.dicoding.capstone.databinding.ItemClassBinding

class ClassAdapter(private val classList: List<ClassDetails>) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.tvClassName)
        val subject: TextView = itemView.findViewById(R.id.tvSubject)
        val classCode: TextView = itemView.findViewById(R.id.tvClassCode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classItem = classList[position]
        holder.className.text = classItem.className
        holder.subject.text = classItem.subject
        holder.classCode.text = classItem.classCode
    }

    override fun getItemCount(): Int = classList.size
}







