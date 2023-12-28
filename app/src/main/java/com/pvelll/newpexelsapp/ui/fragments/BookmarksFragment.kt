package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.repository.DatabaseRepositoryImpl
import com.pvelll.newpexelsapp.ui.viewmodelfactories.BookmarksViewModelFactory
import com.pvelll.newpexelsapp.ui.viewmodels.BookmarksViewModel

class BookmarksFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarksFragment()
    }

    private lateinit var viewModel: BookmarksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoDao = PhotoDatabase.getDatabase(requireContext()).photoDao()
        val repository = DatabaseRepositoryImpl(photoDao)
        viewModel = ViewModelProvider(this, BookmarksViewModelFactory(repository, requireActivity().application))[BookmarksViewModel::class.java]
    }


}