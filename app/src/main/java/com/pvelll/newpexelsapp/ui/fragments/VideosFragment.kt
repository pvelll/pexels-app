package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.model.Video
import com.pvelll.newpexelsapp.databinding.FragmentHomeBinding
import com.pvelll.newpexelsapp.databinding.FragmentVideosBinding
import com.pvelll.newpexelsapp.ui.adapters.HomeRecyclerViewAdapter
import com.pvelll.newpexelsapp.ui.adapters.VideosRecyclerViewAdapter
import com.pvelll.newpexelsapp.ui.utils.SlideInUpAnimator
import com.pvelll.newpexelsapp.ui.viewmodels.VideosViewModel


class VideosFragment : Fragment() {//TODO: for videos use exoplayer


    private lateinit var videoPicAdapter: VideosRecyclerViewAdapter
    private var _binding: FragmentVideosBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var viewModel: VideosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVideosBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        viewModel = ViewModelProvider(this)[VideosViewModel::class.java]
        viewModel.getVideos()
        setupObservers()
        setupRecyclerView()
    }



    private fun setVideos(videos : ArrayList<Video>){
        videoPicAdapter.setVideoData(videos)
    }



    private fun setupObservers() {
        viewModel.videosList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.videos.isNotEmpty()) {
                    setVideos(it.videos as ArrayList<Video>)
                } else {
                    Log.d("VIDEO", "Error getting video")
                }
            } else {
                    // TODO: another error
                }
        })
    }

    private fun setupRecyclerView(){
        videoPicAdapter = VideosRecyclerViewAdapter()
        binding.pictureRecyclerView.apply {
            adapter = videoPicAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            setItemViewCacheSize(1000)
        }
        videoPicAdapter.clearVideoData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance() = VideosFragment()
    }
}