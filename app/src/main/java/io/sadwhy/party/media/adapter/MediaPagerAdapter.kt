package io.sadwhy.party.media.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.Disposable
import coil3.toBitmap
import com.davemorrissey.labs.subscaleview.ImageSource
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import kotlin.math.min

class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int) -> Unit,
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
        imageLoader = ImageLoader(recyclerView.context)
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
        val url = imageUrls[position]
        
        loadImage(url, holder)
    }

    override fun onViewDetachedFromWindow(holder: ImageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.postImage.recycle()
        holder.requestDisposable?.dispose()
    }

    override fun onViewAttachedToWindow(holder: ImageViewHolder) {
        super.onViewAttachedToWindow(holder)
        val pos = holder.bindingAdapterPosition
        val url = imageUrls[pos]
        loadImage(url, holder)
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.postImage.recycle()
    }

    private fun loadImage(
        imageUrl: String,
        holder: ImageViewHolder
    ) {
        val photoView = holder.binding.postImage
        val request = ImageRequest.Builder(photoView.context)
            .data(imageUrl)
            .target { drawable ->
                val bitmap = drawable.toBitmap()
                photoView.setImage(ImageSource.bitmap(bitmap))
                photoView.doOnLayout {
                    val pos = holder.bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION && heightCache[pos] == null) {
                        val finalH = calculateHeight(bitmap, photoView.width)
                        heightCache[pos] = finalH
                        onImageHeightReady(finalH)
                    }
                }
            }
            .build()

        holder.requestDisposable = imageLoader.enqueue(request)
    }

    private fun calculateHeight(bitmap: Bitmap, viewWidth: Int): Int {
        val ratio = bitmap.height.toFloat() / bitmap.width
        val clampedRatio = min(ratio, 16f / 9f)
        return (viewWidth * clampedRatio).toInt()
    }
}