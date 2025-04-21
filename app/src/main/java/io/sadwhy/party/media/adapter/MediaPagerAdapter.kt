package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import io.sadwhy.party.media.model.Post
import io.sadwhy.party.databinding.ItemPostPhotoBinding

class MediaPagerAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<MediaPagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemPostPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val photoView = holder.binding.postImage
        photoView.load(imageUrls[position]) {
            crossfade(true)
        }
    }

    override fun getItemCount(): Int = imageUrls.size
}