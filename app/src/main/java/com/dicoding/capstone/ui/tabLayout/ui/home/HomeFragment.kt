package com.dicoding.capstone.ui.tabLayout.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstone.adapter.ClassAdapter
import com.dicoding.capstone.data.model.ClassModel
import com.dicoding.capstone.databinding.FragmentHomeBinding
import com.dicoding.capstone.ui.detail.CameraActivity
import com.dicoding.capstone.ui.detail.DetailActivity
import com.dicoding.capstone.util.helper.OnClassItemClickListener
import com.dicoding.capstone.viewModel.ClassListViewModel

class HomeFragment : Fragment(), OnClassItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val classListViewModel: ClassListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        // Fetch class list from Firestore
        classListViewModel.fetchClassList()

        // Observe class list and update RecyclerView
        classListViewModel.classList.observe(viewLifecycleOwner, Observer { classList ->
            (binding.rvClass.adapter as ClassAdapter).submitList(classList)
        })

        return root
    }

    private fun setupRecyclerView() {
        binding.rvClass.layoutManager = LinearLayoutManager(context)
        binding.rvClass.adapter = ClassAdapter(emptyList(), this)
    }

    override fun onClassItemClick(item: ClassModel) {
        // Handle item click event
        Toast.makeText(context, "Clicked: ${item.kelas}", Toast.LENGTH_SHORT).show()
        // Navigate to another activity or fragment
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("CLASS_DATA", item)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
