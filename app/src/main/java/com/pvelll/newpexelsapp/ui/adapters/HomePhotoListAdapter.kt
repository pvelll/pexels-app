package com.pvelll.newpexelsapp.ui.adapters

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

        fun bind(photo: Photo) {
            binding.authorName.visibility = View.GONE
            itemView.setOnLongClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val file =
                        File(binding.root.context.getExternalFilesDir(null), "${photo.id}.jpeg")
                    try {
                        val db = Room.databaseBuilder(
                            binding.root.context,
                            PhotoDatabase::class.java, "photos"
                        ).build()
                        val existingPhoto = db.photoDao().getById(photo.id)
                        if (existingPhoto != null && file.exists()) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    binding.root.context,
                                    "photo exists",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val bytes = URL(photo.src.large2x).readBytes()
                            val outputStream = FileOutputStream(file)
                            outputStream.use {
                                it.write(bytes)
                            }
                            db.photoDao().insert(photo)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    binding.root.context,
                                    "successfully added to bookmarks",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(binding.root.context, "error", Toast.LENGTH_SHORT).show()
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
    }
}