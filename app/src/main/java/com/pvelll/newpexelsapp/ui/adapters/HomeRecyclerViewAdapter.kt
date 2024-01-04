package com.pvelll.newpexelsapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.databinding.ItemPictureBinding
import com.pvelll.newpexelsapp.domain.usecases.OnPhotoClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class HomeRecyclerViewAdapter(
    private val listener: OnPhotoClickListener,
) : RecyclerView.Adapter<HomeRecyclerViewAdapter.PhotoViewHolder>() {
    private var photoList = mutableListOf<Photo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]
        holder.bind(photo)
        holder.itemView.setOnClickListener {
            listener.onPhotoClick(photo)
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    fun setPhotoData(photos: List<Photo>) {
        photoList.clear()
        photoList.addAll(photos)
        notifyDataSetChanged()
    }

    fun clearPictureData() {
        photoList.clear()
        notifyDataSetChanged()
    }

    class PhotoViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val db by inject<PhotoDatabase>(PhotoDatabase::class.java)
        fun bind(photo: Photo) {
            binding.authorName.visibility = View.GONE
            binding.photoCardView.setOnLongClickListener {
                CoroutineScope(Dispatchers.Default).launch {
                    val file =
                        File(binding.root.context.getExternalFilesDir(null), "${photo.id}.jpeg")
                    if (file.exists()) {
                        showToast("photo exists")
                    } else {
                        try {
                            savePhoto(file, photo)
                        } catch (e: IOException) {
                            showToast("error")
                        }
                    }
                }
                true
            }
            Glide.with(itemView)
                .load(photo.src.large2x)
                .placeholder(R.drawable.default_card_image)
                .into(binding.photoImage)
        }

        private suspend fun savePhoto(file: File, photo: Photo) {
            val bytes = URL(photo.src.large2x).readBytes()
            val outputStream = FileOutputStream(file)
            Log.d("Saving photo", "photo saved")
            outputStream.use {
                it.write(bytes)
            }
            db.photoDao().insert(photo)
            showToast("successfully added to bookmarks")
        }

        private suspend fun showToast(message: String) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    binding.root.context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}