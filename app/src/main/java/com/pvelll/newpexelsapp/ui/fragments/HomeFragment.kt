package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.databinding.FragmentHomeBinding
import com.pvelll.newpexelsapp.domain.connectivity.ConnectivityObserver
import com.pvelll.newpexelsapp.domain.usecases.OnPhotoClickListener
import com.pvelll.newpexelsapp.ui.adapters.HomeRecyclerViewAdapter
import com.pvelll.newpexelsapp.ui.viewmodelfactories.HomeViewModelFactory
import com.pvelll.newpexelsapp.ui.viewmodels.HomeViewModel
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(), OnPhotoClickListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var photoAdapter: HomeRecyclerViewAdapter
    private lateinit var connectivityObserver: ConnectivityObserver
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val api by inject<PexelApi>()
        connectivityObserver = NetworkConnectivityObserver(requireContext())
        val factory = HomeViewModelFactory(
            api,
            connectivityObserver as NetworkConnectivityObserver
        )
        onConnectivityError()
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun onConnectivityError() {
        if (!(connectivityObserver as NetworkConnectivityObserver).isConnected()) {
            binding.mainHomeLayout.visibility = View.GONE
            binding.noNetworkLayout.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "No network connection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPhotoClick(photo: Photo) {
        //todo: imgae click processing
    }

    private fun setPhotos(items: ArrayList<Photo>) {
        photoAdapter.setPhotoData(items)
        binding.pictureRecyclerView.visibility = View.VISIBLE
        binding.noDataLayout.visibility = View.GONE

    }
    private fun setupObservers() {
        viewModel.pictureList.observe(viewLifecycleOwner, Observer {
            val response = it
            if (it != null) {
                if (response.photos.isNotEmpty()) {
                    setPhotos(response.photos as ArrayList<Photo>)
                } else {
                    showStub()
                }
            } else {
                if (!(connectivityObserver as NetworkConnectivityObserver).isConnected()) {
                    onConnectivityError()
                } else {
                    // TODO: another error
                }
            }
        })

        viewModel.curatedPhotosList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val response = it
                setPhotos(response.photos as ArrayList<Photo>)
            } else {
                if (!(connectivityObserver as NetworkConnectivityObserver).isConnected()) {
                    onConnectivityError()
                } else {
                    // TODO: another error
                }
            }
        })
        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer { progress ->

            binding.progressBar.progress = progress
            binding.progressBar.visibility =
                if (progress < 100) View.VISIBLE else View.GONE
        })
        viewModel.currentQuery.observe(viewLifecycleOwner, Observer {
            for (i in 0 until binding.scrollLinearLayout.childCount) {
                val child = binding.scrollLinearLayout.getChildAt(i)
            }
        })
    }

    private fun setupListeners() {
        binding.searchBar.queryHint = "Search"
        binding.searchBar.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private var handler = Handler(Looper.getMainLooper())
            private val runnable = Runnable {
                onConnectivityError()
                viewModel.currentQuery.value = searchQuery
                if (searchQuery == "") {
                    viewModel.getCuratedPhotos()
                } else {
                    viewModel.getPicture(searchQuery)
                }

            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.currentQuery.value = query
                searchQuery = query
                runnable.run()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.currentQuery.value = newText
                searchQuery = newText
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 500)
                return false
            }
        })
        binding.tryAgainButton.setOnClickListener {
            if ((connectivityObserver as NetworkConnectivityObserver).isConnected()) {
                if (searchQuery.isNotEmpty()) {
                    viewModel.getPicture(searchQuery)
                } else {
                    viewModel.getCuratedPhotos()
                    viewModel.getGalleries()
                }
                binding.noNetworkLayout.visibility = View.GONE
                binding.mainHomeLayout.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "No network connection", Toast.LENGTH_SHORT).show()
            }
        }
        binding.exploreButton.setOnClickListener {
            viewModel.getCuratedPhotos()
            clearSearchBar()
            binding.noDataLayout.visibility = View.GONE
            binding.pictureRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        photoAdapter = HomeRecyclerViewAdapter(this)
        binding.pictureRecyclerView.apply {
            adapter = photoAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            setItemViewCacheSize(1000)
        }
        photoAdapter.clearPictureData()
    }

    private fun showStub() {
        binding.pictureRecyclerView.visibility = View.GONE
        binding.noDataLayout.visibility = View.VISIBLE
        binding.exploreButton.setOnClickListener {
            viewModel.getCuratedPhotos()
            clearSearchBar()
            binding.noDataLayout.visibility = View.GONE
            binding.pictureRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun clearSearchBar() {
        binding.searchBar.setQuery("", false)
    }
}