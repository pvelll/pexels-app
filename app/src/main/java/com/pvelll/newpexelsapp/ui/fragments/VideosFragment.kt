package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.ui.viewmodels.VideosViewModel


class VideosFragment : Fragment() {//TODO: for videos use exoplayer

    companion object {
        fun newInstance() = VideosFragment()
    }

    private lateinit var viewModel: VideosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        viewModel = ViewModelProvider(this)[VideosViewModel::class.java]
    }

}