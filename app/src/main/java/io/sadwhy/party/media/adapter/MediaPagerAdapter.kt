package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.request.CachePolicy
import coil3.request.Disposable
import coil3.request.ImageRequest
import coil3.size.Size
import coil3.imageLoader
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import okio.Path.Companion.toFile
import kotlin.math.min

class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int) -> Unit
) : RecyclerView.Adapter<MediaPagerAdapter.ImageViewHolder>() {

    private val heightCache = mutableMapOf<Int, Int>()
    private lateinit var imageLoader: ImageLoader

    fun getHeightForPosition(position: Int): Int? = heightCache[position]

    inner class ImageViewHolder(val binding: ItemPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var requestDisposable: Disposable? = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        imageLoader = recyclerView.context.imageLoader
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.requestDisposable?.dispose()
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

        // Cancel any previous request and clear the view
        holder.requestDisposable?.dispose()
        photoView.recycle()

        val request = ImageRequest.Builder(holder.itemView.context)
            .data(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .size(Size.ORIGINAL)
            .listener(
                onSuccess = { _, result ->
                    result.diskCacheKey?.let { key ->
                        handleImageSuccess(photoView, holder, key)
                    }
                },
                onError = { _, _ ->
                    // TODO
                }
            )
            .build()

        holder.requestDisposable = imageLoader.enqueue(request)
    }

    private fun handleImageSuccess(
        photoView: SubsamplingScaleImageView,
        holder: ImageViewHolder,
        diskCacheKey: String
    ) {
        imageLoader.diskCache?.openSnapshot(diskCacheKey)?.use { snapshot ->
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
        photoView: SubsamplingScaleImageView
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