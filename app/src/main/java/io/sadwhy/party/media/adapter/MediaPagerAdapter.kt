package io.sadwhy.party.media.adapter

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
    private lateinit var imageLoader: ImageLoader

    fun getHeightForPosition(position: Int): Int? = heightCache[position]

    inner class ImageViewHolder(val binding: ItemPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
      var request: Disposable? = null
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        imageLoader = ImageLoader(rv.context) // single shared loader
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = 
      ImageViewHolder(
        ItemPostPhotoBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )

    override fun getItemCount() = imageUrls.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val photoView = holder.binding.postImage

        // cancel any old request & clear SSIV
        holder.request?.dispose()
        photoView.reset()

        // load (from memory/disk cache if available)
        val req = ImageRequest.Builder(photoView.context)
          .data(imageUrls[position])
          .target(
            onSuccess = { result ->
              val bitmap = result.toBitmap()
              photoView.setImage(ImageSource.bitmap(bitmap))

              photoView.doOnLayout {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION && heightCache[pos] == null) {
                  val ratio = bitmap.height.toFloat() / bitmap.width
                  val clamped = min(ratio, 16f/9f)
                  val h = (photoView.width * clamped).toInt()
                  heightCache[pos] = h
                  onImageHeightReady(h)
                }
              }
            }
          )
          .build()

        holder.request = imageLoader.enqueue(req)
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.request?.dispose()
        holder.binding.postImage.recycle()
    }
}