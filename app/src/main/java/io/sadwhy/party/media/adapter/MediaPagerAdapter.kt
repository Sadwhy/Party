package io.sadwhy.party.media.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import kotlin.math.min

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
        val context = photoView.context
        val imageLoader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(imageUrls[position])
            .target(
                onSuccess = { result ->
                    val drawable = result.drawable
                    photoView.setImageDrawable(drawable)
        
                    val bitmap = (drawable as? BitmapDrawable)?.bitmap
                    if (bitmap != null) {
                        val imageWidth = bitmap.width
                        val imageHeight = bitmap.height
                        val ratio = imageHeight.toFloat() / imageWidth
                        val maxRatio = 16f / 9f
                        val viewWidth = photoView.width.takeIf { it > 0 } ?: photoView.measuredWidth
        
                        if (viewWidth > 0) {
                            val clampedRatio = min(ratio, maxRatio)
                            val finalHeight = (viewWidth * clampedRatio).toInt()
                            onImageHeightReady(finalHeight)
                        }
                    }
                }
            )
            .build()

        imageLoader.enqueue(request)
    }

    override fun getItemCount(): Int = imageUrls.size
}