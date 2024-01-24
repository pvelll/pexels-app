package com.pvelll.newpexelsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.model.Video
import com.pvelll.newpexelsapp.data.repository.PhotoStorageRepositoryImpl
import com.pvelll.newpexelsapp.databinding.ItemPictureBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class VideosRecyclerViewAdapter(
) : RecyclerView.Adapter<VideosRecyclerViewAdapter.PhotoViewHolder>() {
    private var videoList = mutableListOf<Video>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val video = videoList[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    fun setVideoData(videos: List<Video>) {
        videoList.clear()
        videoList.addAll(videos)
        notifyDataSetChanged()
    }

    fun clearVideoData() {
        videoList.clear()
        notifyDataSetChanged()
    }

    class PhotoViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video : Video) {
            binding.authorName.visibility = View.GONE
//            binding.photoCardView.setOnLongClickListener {
//                CoroutineScope(Dispatchers.Default).launch {
//                    val file =
//                        File(binding.root.context.getExternalFilesDir(null), "${photo.id}.jpeg")
//                    if (file.exists()) {
//                        showToast("photo exists")
//                    } else {
//                        try {
//                            photoStorageRepo.saveToBookmarks(photo,file)
//                        } catch (e: IOException) {
//                            showToast("error")
//                        }
//                    }
//                }
//                true
//            }
            Glide.with(itemView)
                .load(video.video_pictures[0].picture)
                .placeholder(R.drawable.default_card_image)
                .into(binding.photoImage)
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