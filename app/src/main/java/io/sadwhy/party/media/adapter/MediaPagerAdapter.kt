package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
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

    fun getHeightForPosition(position: Int): Int? = heightCache[position]

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

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
    ) {
        val photoView = holder.binding.postImage
        val context = photoView.context
        val loader = ImageLoader(context)

        val request =
            ImageRequest
                .Builder(context)
                .data(imageUrls[position])
                .target(
                    onSuccess = { drawable ->
                        val bitmap = drawable.toBitmap()
                        photoView.setImage(ImageSource.bitmap(bitmap))

                        val ratio = bitmap.height.toFloat() / bitmap.width
                        val clampedRatio = min(ratio, 16f / 9f)
                        val viewWidth = photoView.width.takeIf { it > 0 } ?: photoView.measuredWidth

                        if (viewWidth > 0 && heightCache[position] == null) {
                            val finalHeight = (viewWidth * clampedRatio).toInt()
                            heightCache[position] = finalHeight
                            onImageHeightReady(finalHeight)
                        }
                    },
                ).build()

        loader.enqueue(request)
    }
}
