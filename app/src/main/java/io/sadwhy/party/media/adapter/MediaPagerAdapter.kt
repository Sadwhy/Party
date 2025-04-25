package io.sadwhy.party.media.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.Disposable
import coil3.request.ImageRequest
import coil3.toBitmap
import com.davemorrissey.labs.subscaleview.ImageSource
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import kotlin.math.min

class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int) -> Unit,
) : RecyclerView.Adapter<MediaPagerAdapter.ImageViewHolder>() {

    private val heightCache = mutableMapOf<Int, Int>()
    private val requestMap = mutableMapOf<RecyclerView.ViewHolder, Disposable>()
    private lateinit var imageLoader: ImageLoader

    fun getHeightForPosition(position: Int): Int? = heightCache[position]

    inner class ImageViewHolder(val binding: ItemPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        imageLoader = ImageLoader(recyclerView.context)
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        // Cancel ongoing image request
        requestMap.remove(holder)?.dispose()

        // Explicitly clear the image and free memory
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

        // Cancel previous request if any
        requestMap.remove(holder)?.dispose()

        val request = ImageRequest.Builder(photoView.context)
            .data(imageUrl)
            .allowHardware(false) // important: ensures software bitmap
            .target(
                onSuccess = { drawable ->
                    try {
                        val bitmap = drawable.toBitmap()
                        photoView.setImage(ImageSource.bitmap(bitmap))

                        photoView.doOnLayout {
                            val adapterPosition = holder.bindingAdapterPosition
                            if (adapterPosition != RecyclerView.NO_POSITION &&
                                heightCache[adapterPosition] == null
                            ) {
                                val finalHeight = calculateHeight(bitmap, photoView.width)
                                heightCache[adapterPosition] = finalHeight
                                onImageHeightReady(finalHeight)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                onError = {
                    // Handle failure
                }
            )
            .build()

        val disposable = imageLoader.enqueue(request)
        requestMap[holder] = disposable
    }

    private fun calculateHeight(bitmap: Bitmap, viewWidth: Int): Int {
        val ratio = bitmap.height.toFloat() / bitmap.width
        val clampedRatio = min(ratio, 16f / 9f)
        return (viewWidth * clampedRatio).toInt()
    }
}