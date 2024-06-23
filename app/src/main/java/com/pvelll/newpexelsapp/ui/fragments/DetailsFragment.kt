package com.pvelll.newpexelsapp.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.databinding.FragmentDetailsBinding
import com.pvelll.newpexelsapp.ui.viewmodels.DetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class DetailsFragment() : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        val args: DetailsFragmentArgs by navArgs()
        viewModel.init(args.photo)
        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveToBookmarks.setOnClickListener {
            viewModel.saveToBookmarks()
        }

        binding.downloadButton.setOnClickListener {
            viewModel.downloadPhoto()
        }

        binding.exploreButton.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }


    private fun setupObservers() {
        viewModel.photo.observe(viewLifecycleOwner) { photo ->
            binding.photoAuthor.text = photo.photographer
            loadPhoto(photo)
        }

        viewModel.isBookmarked.observe(viewLifecycleOwner) { isBookmarked ->
            if (isBookmarked) {
                binding.saveToBookmarks.setImageResource(R.drawable.ic_bookmarks_active)
            } else {
                binding.saveToBookmarks.setImageResource(R.drawable.ic_bookmarks_inactive)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPhoto(photo: Photo) {
        if (viewModel.isNetworkAvailable.value == true) {
            Glide.with(binding.pictureCardView)
                .load(photo.src.large2x)
                .placeholder(R.drawable.default_card_image)
                .into(binding.pictureView)
        } else {
            try {
                val file = File(requireContext().getExternalFilesDir(null), "${photo.id}.jpeg")
                if (file.exists()) {
                    Glide.with(requireContext())
                        .load(file)
                        .placeholder(R.drawable.default_card_image)
                        .into(binding.pictureView)

                } else {
                    onImageNotFound()
                }
            } catch (e: Exception) {
                onImageNotFound()
            }

        }
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
