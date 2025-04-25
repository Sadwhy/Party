package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Size
import coil3.imageLoader
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
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
        // Cancel any ongoing requests for this view
        imageLoader.cancel(holder.binding.postImage)
        holder.binding.postImage.recycle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemPostPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val photoView = holder.binding.postImage
        val imageUrl = imageUrls[position]

        // Clear previous image
        photoView.recycle()

        val request = ImageRequest.Builder(holder.itemView.context)
            .data(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .size(Size.ORIGINAL)
            .target(photoView)  // Use proper target binding
            .listener(
                onSuccess = { _, result ->
                    handleImageSuccess(photoView, holder, result.diskCacheKey)
                },
                onError = { _, throwable ->
                    // Handle error state
                }
            )
            .build()

        imageLoader.enqueue(request)
    }

    private fun handleImageSuccess(
        photoView: SubsamamplingScaleImageView,
        holder: ImageViewHolder,
        diskCacheKey: String?
    ) {
        if (diskCacheKey == null) return

        // Access disk cache correctly in Coil 3
        imageLoader.diskCache?.get(diskCacheKey)?.use { snapshot: DiskCache.Snapshot ->
            val filePath = snapshot.data.toFile().absolutePath
            
            photoView.post {
                if (holder.bindingAdapterPosition == RecyclerView.NO_POSITION) return@post
                
                photoView.setImage(ImageSource.uri(filePath))
                calculateAndStoreHeight(holder, photoView)
            }
        }
    }

    private fun calculateAndStoreHeight(
        holder: ImageViewHolder,
        photoView: SubsamamplingScaleImageView
    ) {
        photoView.doOnLayout {
            val position = holder.bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                ?: return@doOnLayout

            if (!heightCache.containsKey(position)) {
                val width = photoView.sWidth
                val height = photoView.sHeight
                if (width > 0 && height > 0) {
                    val ratio = min(height.toFloat() / width, 16f / 9f)
                    val finalHeight = (photoView.width * ratio).toInt()
                    heightCache[position] = finalHeight
                    onImageHeightReady(finalHeight)
                }
            }
        }
    }
}