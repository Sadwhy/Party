package io.sadwhy.party.media.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import io.sadwhy.party.databinding.ItemPostPhotoBinding
import kotlin.math.min

class MediaPagerAdapter(
    private val imageUrls: List<String>,
    private val onImageHeightReady: (Int, Int) -> Unit,
) : RecyclerView.Adapter<MediaPagerAdapter.ImageViewHolder>() {
    
    // height cache
    private val heightCache = mutableMapOf<Int, Int>()
    
    inner class ImageViewHolder(
        val binding: ItemPostPhotoBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageViewHolder {
        val binding = ItemPostPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
    ) {
        val photoView = holder.binding.postImage
        val context = photoView.context
        
        // use cached height if found
        heightCache[position]?.let { height ->
            onImageHeightReady(height, position)
        }
        
        loadImage(context, imageUrls[position], photoView, position)
    }
    
    private fun loadImage(context: Context, url: String, photoView: SubsamplingScaleImageView, position: Int) {
        val imageLoader = ImageLoader(context)
        
        val request = ImageRequest
            .Builder(context)
            .data(url)
            .target(
                onSuccess = { result ->
                    val bitmap = (result as SuccessResult).toBitmap()
                    photoView.setImage(ImageSource.bitmap(bitmap))

                    val imageWidth = bitmap.width
                    val imageHeight = bitmap.height
                    val ratio = imageHeight.toFloat() / imageWidth
                    val maxRatio = 16f / 9f
                    val viewWidth = photoView.width.takeIf { it > 0 } ?: photoView.measuredWidth

                    if (viewWidth > 0) {
                        val clampedRatio = min(ratio, maxRatio)
                        val finalHeight = (viewWidth * clampedRatio).toInt()
                        
                        // Cache the calculated height
                        heightCache[position] = finalHeight
                        
                        onImageHeightReady(finalHeight, position)
                    }
                },
            ).build()

        imageLoader.enqueue(request)
    }

    override fun getItemCount(): Int = imageUrls.size
}