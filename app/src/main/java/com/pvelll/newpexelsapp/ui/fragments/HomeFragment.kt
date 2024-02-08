package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.model.Collection
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.databinding.FragmentHomeBinding
import com.pvelll.newpexelsapp.domain.connectivity.ConnectivityObserver
import com.pvelll.newpexelsapp.domain.usecases.OnPhotoClickListener
import com.pvelll.newpexelsapp.ui.adapters.HomeRecyclerViewAdapter
import com.pvelll.newpexelsapp.ui.utils.SlideInUpAnimator
import com.pvelll.newpexelsapp.ui.viewmodels.HomeViewModel
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(), OnPhotoClickListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var photoAdapter: HomeRecyclerViewAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    private var selectedView: View? = null
    private var selectedTitle: String? = null
    private var previousSelectedView: View? = null
    private var previousTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            selectedTitle = arguments?.getString(SELECTED_TITLE_KEY)
            val currentQuery = viewModel.currentQuery.value
            val photoList = viewModel.pictureList.value?.photos
            if (currentQuery != null) {
                binding.searchBar.setQuery(currentQuery, false)
            }
            if (photoList != null) {
                setPhotos(photoList)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        onConnectivityError()
        binding.scrollLinearLayout.orientation = LinearLayout.HORIZONTAL
        viewModel.getGalleries()
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun onConnectivityError() {
        if (viewModel.isNetworkAvailable.value == false) {
            binding.mainHomeLayout.visibility = View.GONE
            binding.noNetworkLayout.visibility = View.VISIBLE
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPhotoClick(photo: Photo) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    private fun setPhotos(items: List<Photo>) {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
        photoAdapter.setPhotoData(items)
        binding.pictureRecyclerView.visibility = View.VISIBLE
        binding.noDataLayout.visibility = View.GONE
    }

    private fun setGalleries(items: List<Collection>) {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
        binding.scrollLinearLayout.removeAllViews()
        items.forEach {
            val item = it
            val newItem: View = layoutInflater.inflate(R.layout.item_gallery_topic, null)
            val textView: TextView = newItem.findViewById(R.id.featured_topic_text)
            textView.text = item.title
            newItem.setOnClickListener {
                previousSelectedView?.background =
                    context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.gallery_selector
                        )
                    }
                previousTextView?.setTextColor(resources.getColor(R.color.text))
                selectedView = newItem
                viewModel.searchByTitle(item.title)
                selectedView!!.background = context?.let { it1 ->
                    ContextCompat.getDrawable(
                        it1,
                        R.drawable.gallery_item_selected
                    )
                }
                textView.setTextColor(resources.getColor(R.color.text))
                previousSelectedView = selectedView
                previousTextView = textView
                binding.searchBar.setQuery(item.title, false)
            }
            binding.scrollLinearLayout.addView(newItem)
        }
    }


    private fun setupObservers() {
        viewModel.pictureList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.photos.isNotEmpty()) {
                    setPhotos(it.photos)
                } else {
                    showStub()
                }
            } else {
                if (viewModel.isNetworkAvailable.value == false) {
                    onConnectivityError()
                } else {
                    // TODO: another error
                }
            }
        })
        viewModel.galleryList.observe(viewLifecycleOwner, Observer {
            if (it?.collections != null) {
                setGalleries(it.collections)
            } else {
                if (viewModel.isNetworkAvailable.value == false) {
                    onConnectivityError()
                } else {
                    // TODO: another error
                }
            }
        })
        viewModel.curatedPhotosList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setPhotos(it.photos)
            } else {
                if (viewModel.isNetworkAvailable.value == false) {
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
                val textView: TextView = child.findViewById(R.id.featured_topic_text)

                if (textView.text.toString().equals(it, ignoreCase = true)) {
                    child.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.gallery_item_selected
                    )
                    textView.setTextColor(resources.getColor(R.color.text))

                    selectedView = child
                    selectedTitle = textView.text.toString()
                    previousSelectedView = selectedView
                    previousTextView = textView
                } else {

                    child.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.gallery_selector
                    )
                    textView.setTextColor(resources.getColor(R.color.text))
                }
            }
        })

        viewModel.isNetworkAvailable.observe(viewLifecycleOwner, Observer { onConnectivityError() })
    }

    private fun setupListeners() {
        binding.searchBar.queryHint = resources.getString(R.string.search)
        binding.searchBar.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchByQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchByQuery(newText)
                if (viewModel.isNetworkAvailable.value == true) {
                    if (newText != selectedTitle) {
                        selectedTitle = null
                        selectedView?.background =
                            resources.getDrawable(R.drawable.gallery_selector)
                        selectedView?.findViewById<TextView>(R.id.featured_topic_text)
                            ?.setTextColor(resources.getColor(R.color.text))
                    }
                }
                return false
            }
        })
        binding.tryAgainButton.setOnClickListener {
            if (viewModel.isNetworkAvailable.value == true) {
                viewModel.refreshData()
                binding.noNetworkLayout.visibility = View.GONE
                binding.mainHomeLayout.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.no_internet),
                    Toast.LENGTH_SHORT
                ).show()
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
        photoAdapter = context?.let { HomeRecyclerViewAdapter(this, it) }!!
//        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
        binding.pictureRecyclerView.apply {
            adapter = photoAdapter
            itemAnimator = SlideInUpAnimator()
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
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SELECTED_TITLE_KEY, selectedTitle)
        outState.putString(CURRENT_QUERY_KEY, binding.searchBar.query.toString())
        outState.putParcelableArrayList(PHOTO_LIST_KEY, photoAdapter.getPhotoData())
    }


    companion object {
        private const val SELECTED_TITLE_KEY = "selected_title"
        private const val CURRENT_QUERY_KEY = "current_query"
        private const val PHOTO_LIST_KEY = "photo_list"
    }
}