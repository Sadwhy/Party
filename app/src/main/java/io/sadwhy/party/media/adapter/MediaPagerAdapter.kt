package io.sadwhy.party.media.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.Disposable
import coil3.toBitmap
import coil3.memory.MemoryCache
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
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
        
        fun attachBitmapToView(bitmap: Bitmap) {
            binding.postImage.setImage(ImageSource.bitmap(bitmap))
            binding.postImage.doOnLayout {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION && heightCache[pos] == null) {  
                    val finalH = calculateHeight(bitmap, binding.postImage.width)  
                    heightCache[pos] = finalH  
                    onImageHeightReady(finalH)  
                }
            }
        }
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
        
        // Configure SSIV with any default settings needed
        binding.postImage.apply {
            setOnImageEventListener(object : SubsamplingScaleImageView.OnImageEventListener {
                override fun onReady() {}
                override fun onImageLoaded() {}
                override fun onPreviewLoaded() {}
                override fun onPreviewReleased() {}
                override fun onTileLoaded() {}
                override fun onImageLoadError(e: Exception) {}
                override fun onPreviewLoadError(e: Exception) {}
                override fun onTileLoadError(e: Exception) {}
            })
        }
        
        return ImageViewHolder(binding)  
    }  

    override fun getItemCount(): Int = imageUrls.size  

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {  
        val imageUrl = imageUrls[position]

        // Check if bitmap is already in memory cache
        val cacheKey = MemoryCache.Key(imageUrl)
        val cachedBitmap = imageLoader.memoryCache?.get(cacheKey)?.bitmap
        
        if (cachedBitmap != null) {
            // Use cached bitmap directly
            holder.attachBitmapToView(cachedBitmap)
        } else {
            // Load the image if not cached
            val request = ImageRequest.Builder(holder.binding.root.context)  
                .data(imageUrl)
                .memoryCacheKey(cacheKey)
                .target { drawable ->  
                    val bitmap = drawable.toBitmap()
                    holder.attachBitmapToView(bitmap)
                }  
                .build()  

            holder.requestDisposable = imageLoader.enqueue(request)
        }
    }  

    override fun onViewDetachedFromWindow(holder: ImageViewHolder) {  
        super.onViewDetachedFromWindow(holder)  
        holder.binding.postImage.recycle()  
        holder.requestDisposable?.dispose()  
    }  

    override fun onViewAttachedToWindow(holder: ImageViewHolder) {  
        super.onViewAttachedToWindow(holder)
        
        // Reload image if needed when view is reattached to window
        val position = holder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val imageUrl = imageUrls[position]
            val cacheKey = MemoryCache.Key(imageUrl)
            val cachedBitmap = imageLoader.memoryCache?.get(cacheKey)?.bitmap
            
            if (cachedBitmap != null && holder.binding.postImage.isImageLoaded.not()) {
                holder.attachBitmapToView(cachedBitmap)
            }
        }
    }  

    private fun calculateHeight(bitmap: Bitmap, viewWidth: Int): Int {  
        val ratio = bitmap.height.toFloat() / bitmap.width  
        val clampedRatio = min(ratio, 16f / 9f)  
        return (viewWidth * clampedRatio).toInt()  
    }
}