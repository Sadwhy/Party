package io.sadwhy.party.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.sadwhy.party.databinding.ItemPostBinding
import io.sadwhy.party.media.model.Post

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        with(holder.binding) {
        
        val imageUrls =
            listOf(
                "https://d.fixupx.com/DarkOddCon/status/1912812028210913632",
                "https://d.fixupx.com/lazycataw/status/1912738980879016263",
                "https://coomer.su/data/07/cf/07cf242a44eef7f1d968241638159f971c056d659150304916584e9ddf04f097.jpg?f=444487380599058432.png",
                "https://coomer.su/data/04/4a/044a45a2757fd6a77a6df8ae0c23ddf4a7868dde405bfe137e6dbaa3289b6fef.jpg?f=444487209794416641.png",
                "https://coomer.su/data/36/e0/36e09ed160da32adb5b246d71ecf9051e278a59d5d38ec1ba956fcc24de52f37.jpg?f=444487320121389056.png"
            )

            holder.binding.mediaPager.adapter = MediaPagerAdapter(imageUrls)
            
            buttonUsername.text = post.username
            textDescription.text = post.description

            // Set up click listeners if needed
            buttonPfp.setOnClickListener {
                // Handle profile pic click
            }

            buttonMore.setOnClickListener {
                // Handle more options
            }

            toggleLike.setOnCheckedChangeListener { _, isChecked ->
                // Handle like toggle
            }

            toggleBookmark.setOnCheckedChangeListener { _, isChecked ->
                // Handle bookmark toggle
            }

            buttonShare.setOnClickListener {
                // Handle share
            }

            // Optionally setup mediaPager adapter
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.username == newItem.username // or unique ID
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}