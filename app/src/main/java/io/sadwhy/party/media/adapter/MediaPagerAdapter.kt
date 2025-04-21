package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable
import coil3.request.target.Target
import coil3.request.ImageRequest
import coil3.load
import kotlin.math.minOf
import io.sadwhy.party.media.model.Post
import io.sadwhy.party.databinding.ItemPostPhotoBinding


class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int) -> Unit
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
            target { drawable ->
                photoView.setImageDrawable(drawable)

                val imageWidth = drawable.intrinsicWidth
                val imageHeight = drawable.intrinsicHeight

                if (imageWidth > 0 && imageHeight > 0) {
                    // Calculate actual image ratio
                    val ratio = imageHeight.toFloat() / imageWidth
                    val maxRatio = 16f / 9f
                    val viewWidth = photoView.width.takeIf { it > 0 } ?: photoView.measuredWidth

                    if (viewWidth > 0) {
                        val clampedRatio = minOf(ratio, maxRatio)
                        val finalHeight = (viewWidth * clampedRatio).toInt()
                        onImageHeightReady(finalHeight)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = imageUrls.size
}