package com.pvelll.newpexelsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.databinding.ItemPictureBinding
import com.pvelll.newpexelsapp.domain.usecases.OnPhotoClickListener
import java.io.File

class BookmarksRecyclerViewAdapter(
    private val listener: OnPhotoClickListener,
    private val context: Context
) : RecyclerView.Adapter<BookmarksRecyclerViewAdapter.PhotoViewHolder>() {
    init {
        setHasStableIds(true)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    private val currentList: List<Photo>
        get() = differ.currentList

    fun setPictureData(photoList: ArrayList<Photo>) {
        differ.submitList(photoList)
    }
    override fun getItemId(position: Int): Long {
        return currentList[position].id.hashCode().toLong()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPictureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context
        )
    }

    override fun onBindViewHolder(
        holder: PhotoViewHolder,
        position: Int,
    ) {
        val photo = currentList[position]
        holder.bind(photo)
        holder.itemView.setOnClickListener {
            listener.onPhotoClick(photo)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class PhotoViewHolder(private val binding: ItemPictureBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.authorName.visibility = View.VISIBLE
            binding.authorName.text = photo.photographer
            val file = File(context.getExternalFilesDir(null), "${photo.id}.jpeg")
            Glide.with(context)
                .load(file)
                .placeholder(R.drawable.default_card_image)
                .into(binding.photoImage)
        }
    }
}
