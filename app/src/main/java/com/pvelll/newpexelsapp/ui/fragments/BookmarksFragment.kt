package com.pvelll.newpexelsapp.ui.fragments

import android.os.Binder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.repository.DatabaseRepositoryImpl
import com.pvelll.newpexelsapp.databinding.FragmentBookmarksBinding
import com.pvelll.newpexelsapp.domain.usecases.OnPhotoClickListener
import com.pvelll.newpexelsapp.ui.adapters.BookmarksRecyclerViewAdapter
import com.pvelll.newpexelsapp.ui.viewmodelfactories.BookmarksViewModelFactory
import com.pvelll.newpexelsapp.ui.viewmodels.BookmarksViewModel

class BookmarksFragment : Fragment() , OnPhotoClickListener{

    companion object {
        fun newInstance() = BookmarksFragment()
    }

    private lateinit var viewModel: BookmarksViewModel
    private lateinit var recyclerViewAdapter: BookmarksRecyclerViewAdapter
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            BookmarksViewModelFactory(requireActivity().application)
        )[BookmarksViewModel::class.java]
        onEmptyPhotoList()
        setupRecyclerView()

    }
    private fun setupRecyclerView() {
        recyclerViewAdapter = BookmarksRecyclerViewAdapter( this)
        viewModel.setPictureAdapter(recyclerViewAdapter)
        binding.pictureRecyclerView.apply {
            adapter = recyclerViewAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            setItemViewCacheSize(1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        viewModel.allPhotos.observe(viewLifecycleOwner, Observer { photos ->
            photos?.let {
                recyclerViewAdapter.setPictureData(it as ArrayList<Photo>, requireContext())
                onEmptyPhotoList()
            }
        })

    }

    override fun onPause() {
        super.onPause()
        viewModel.allPhotos.removeObservers(viewLifecycleOwner)
    }
    private fun onEmptyPhotoList() {
        if (viewModel.allPhotos.value?.isEmpty() == true) {
            binding.mainBookmarksLayout.visibility = View.GONE
            binding.nothingFoundLayout.visibility = View.VISIBLE
        }
    }

    override fun onPhotoClick(photo: Photo) {
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToDetailsFragment2(photo)
        findNavController().navigate(action)
    }


}