package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}