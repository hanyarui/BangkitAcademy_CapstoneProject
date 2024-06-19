package com.dicoding.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstone.data.model.ClassModel
import com.dicoding.capstone.databinding.ItemClassBinding
import com.dicoding.capstone.util.helper.OnClassItemClickListener
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class ClassAdapter(
    private var classItems: List<ClassModel>,
    private val listener: OnClassItemClickListener
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: ClassModel) {
            with(binding) {
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                // Convert Firestore Timestamp to Date and format it
                val formattedTime = item.time?.let {
                    formatter.format((it as Timestamp).toDate())
                } ?: ""

                tvJadwal.text = formattedTime
                tvKelas.text = item.kelas
                tvPelajaran.text = item.mapel
                tvGuru.text = item.teacherName

                // Load image if exists
//                Glide.with(root.context)
//                    .load(item.photoUrl)
//                    .into(ivPhoto)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onClassItemClick(classItems[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classItems[position])
    }

    override fun getItemCount(): Int {
        return classItems.size
    }

    fun submitList(newClassItems: List<ClassModel>) {
        classItems = newClassItems
        notifyDataSetChanged()
    }
}
