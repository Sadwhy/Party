package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Size
import coil3.imageLoader
import com.davemorrissey.labs.subscaleview.ImageSource
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import kotlin.math.min

class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int) -> Unit
) : RecyclerView.Adapter<MediaPagerAdapter.ImageViewHolder>() {

    private val heightCache = mutableMapOf<Int, Int>()
    private lateinit var imageLoader: ImageLoader

    fun getHeightForPosition(position: Int): Int? = heightCache[position]

    inner class ImageViewHolder(val binding: ItemPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        imageLoader = recyclerView.context.imageLoader
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.postImage.recycle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemPostPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val photoView = holder.binding.postImage
        val imageUrl = imageUrls[position]

        val request = ImageRequest.Builder(photoView.context)
            .data(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .size(Size.ORIGINAL)
            .build()

        imageLoader.enqueue(request)

        // Once the image is cached, retrieve it and set the image
        imageLoader.components.diskCache
            ?.get(request.diskCacheKey ?: imageUrl)
            ?.use { snapshot ->
                val filePath = snapshot.data.absolutePath
                photoView.setImage(ImageSource.uri(filePath))

                photoView.doOnLayout {
                    val adapterPosition = holder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION && heightCache[adapterPosition] == null) {
                        val width = photoView.sWidth
                        val height = photoView.sHeight
                        val ratio = min(height.toFloat() / width, 16f / 9f)
                        val finalHeight = (photoView.width * ratio).toInt()
                        heightCache[adapterPosition] = finalHeight
                        onImageHeightReady(finalHeight)
                    }
                }
            }
    }
}