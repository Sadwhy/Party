package io.sadwhy.party.utils.function

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.sadwhy.party.media.adapter.MediaPagerAdapter
import io.sadwhy.party.media.model.Post

// Extension function to set up a post RecyclerView
fun RecyclerView.setupPostList(
    context: Context,
    postAdapter: RecyclerView.Adapter<*>,
    posts: List<Post>
) {
    layoutManager = LinearLayoutManager(context)
    adapter = postAdapter
    
    // If the adapter supports submitting a list (like ListAdapter)
    if (postAdapter is androidx.recyclerview.widget.ListAdapter<*, *>) {
        @Suppress("UNCHECKED_CAST")
        (postAdapter as androidx.recyclerview.widget.ListAdapter<Post, *>).submitList(posts)
    }
}

// Extension function to set up a ViewPager2 for media
fun ViewPager2.setupMediaPager(
    imageUrls: List<String>,
    bindingProvider: () -> ViewGroup
) {
    // Create adapter with height adjustment callback
    val mediaPagerAdapter = createMediaPagerAdapter(imageUrls) { height, _ ->
        post {
            val layoutParams = this.layoutParams
            layoutParams.height = height
            this.layoutParams = layoutParams
        }
    }
    
    adapter = mediaPagerAdapter
    
    // page change callback
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            // Trigger adapter to reload the current item's dimensions
            mediaPagerAdapter.notifyItemChanged(position)
        }
    })
}

// Helper function to create MediaPagerAdapter
fun createMediaPagerAdapter(
    imageUrls: List<String>,
    onImageHeightReady: (Int, Int) -> Unit
): MediaPagerAdapter {
    return MediaPagerAdapter(imageUrls, onImageHeightReady)
}