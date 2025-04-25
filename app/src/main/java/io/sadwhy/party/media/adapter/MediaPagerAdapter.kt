package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import kotlin.math.min

class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int) -> Unit
) : RecyclerView.Adapter<MediaPagerAdapter.ImageViewHolder>() {

    
    private val heightCache = mutableMapOf<Int, Int>()

    // Expose the cache
    fun getHeightForPosition(position: Int): Int? =
        heightCache[position]

    inner class ImageViewHolder(val binding: ItemPostPhotoBinding) : RecyclerView.ViewHolder(binding.root)

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
        val context = photoView.context
        val loader = ImageLoader(context)

        heightCache[position]?.let { cachedH ->
            onImageHeightReady(cachedH)
        }

        val request = ImageRequest.Builder(context)
            .data(imageUrls[position])
            .target(
                onSuccess = { drawable ->
                    val bitmap = drawable.toBitmap()
                    photoView.setImage(ImageSource.bitmap(bitmap))

                    // compute aspect ratio and clamp
                    val ratio = bitmap.height.toFloat() / bitmap.width
                    val maxRatio = 16f / 9f
                    val clamped = min(ratio, maxRatio)

                    // photoView.width might be zero first layout pass; use measuredWidth fallback
                    val viewW = photoView.width.takeIf { it > 0 }
                        ?: photoView.measuredWidth
                    if (viewW > 0) {
                        val finalH = (viewW * clamped).toInt()

                        // only call back & cache the very first time
                        if (heightCache[position] == null) {
                            heightCache[position] = finalH
                            onImageHeightReady(finalH)
                        }
                    }
                }
            )
            .build()

        loader.enqueue(request)
    }
}