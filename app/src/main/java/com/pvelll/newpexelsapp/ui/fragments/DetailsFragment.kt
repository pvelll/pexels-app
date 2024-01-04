package com.pvelll.newpexelsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.bumptech.glide.Glide
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.databinding.FragmentDetailsBinding
import com.pvelll.newpexelsapp.ui.viewmodels.DetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class DetailsFragment() : Fragment() {

    private val connectivityObserver = NetworkConnectivityObserver(requireContext())
    private lateinit var viewModel: DetailsViewModel
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val photo = args.photo
    private val db by inject<PhotoDatabase>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.photoAuthor.text = photo.photographer
        checkPhotoSaved()
        setupClickListeners()
        loadPhoto()
    }

    private fun setupClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveToBookmarks.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val file = File(requireContext().getExternalFilesDir(null), "${photo.id}.jpeg")
                try {
                    val existingPhoto = photo.let { it1 -> db.photoDao().getById(it1.id) }
                    if (existingPhoto != null && file.exists()) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "photo exists", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        downloadImage(photo.src.large2x, file)
                        db.photoDao().insert(photo)
                        withContext(Dispatchers.Main) {
                            binding.saveToBookmarks.background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.download_button_shape
                            )
                            setBookmarksActive()
                            Toast.makeText(
                                context,
                                "successfully added to bookmarks",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Log.d("PhotoLog", "${db.photoDao().getaAll().value}")
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        onImageNotFound()
                    }
                }
            }
        }

        binding.downloadButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(directory, "${photo.id}.jpeg")
                try {
                    downloadImage(photo.src.large2x, file)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Saved to images",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun downloadImage(url: String, file: File) {
        val bytes = URL(url).readBytes()
        val outputStream = FileOutputStream(file)
        outputStream.use { it.write(bytes) }
    }

    private fun loadPhoto() {
        if (connectivityObserver.isConnected()) {
            Glide.with(binding.pictureCardView)
                .load(photo.src.large2x)
                .placeholder(R.drawable.default_card_image)
                .into(binding.pictureView)
        } else if (!connectivityObserver.isConnected()) {
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

    private fun checkPhotoSaved() {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File(requireContext().getExternalFilesDir(null), "${photo.id}.jpeg")
            val existingPhoto = photo.let { db.photoDao().getById(photo.id) }
            if (existingPhoto != null && file.exists()) {
                binding.saveToBookmarks.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.download_button_shape)
                CoroutineScope(Dispatchers.Main).launch {
                    setBookmarksActive()
                }
            }
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