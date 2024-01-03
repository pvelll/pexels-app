package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.databinding.FragmentDetailsBinding
import com.pvelll.newpexelsapp.ui.viewmodels.DetailsViewModel

class DetailsFragment() : Fragment() {


    private lateinit var viewModel: DetailsViewModel
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container,false)
        return binding.root
       }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()
        val photo = args.photo
        binding.photoAuthor.text = photo.photographer
        setupClickListeners()
        Glide.with(binding.pictureCardView)
            .load(photo.src.large2x)
            .placeholder(R.drawable.default_card_image)
            .into(binding.pictureView)
    }

    private fun setupClickListeners(){
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }



    private fun setBookmarksActive() {
        binding.saveToBookmarks.setImageResource(R.drawable.ic_bookmarks_active)
    }

    private fun onImageNotFound() {
        binding.saveDownloadLayout.visibility = View.GONE
        binding.pictureCardView.visibility = View.GONE
        binding.onErrorLayout.visibility = View.VISIBLE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}