package io.sadwhy.party.legacy.media.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil3.load
import io.sadwhy.party.databinding.ItemPostBinding
import io.sadwhy.party.data.model.Post

class PostAdapter(
    private val onProfileClick: (Post) -> Unit,
    private val onMoreOptionsClick: (Post) -> Unit,
    private val onLikeToggled: (Post, Boolean) -> Unit,
    private val onBookmarkToggled: (Post, Boolean) -> Unit,
    private val onShareClick: (Post) -> Unit,
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
    ) {
        val post = getItem(position)
        with(holder.binding) {
            // for testing
            val imageUrls = listOf(
                "https://d.fixupx.com/DarkOddCon/status/1912812028210913632",
                "https://d.fixupx.com/lazycataw/status/1912738980879016263",
                "https://coomer.su/data/07/cf/07cf242a44eef7f1d968241638159f971c056d659150304916584e9ddf04f097.jpg?f=444487380599058432.png",
                "https://coomer.su/data/04/4a/044a45a2757fd6a77a6df8ae0c23ddf4a7868dde405bfe137e6dbaa3289b6fef.jpg?f=444487209794416641.png",
                "https://coomer.su/data/36/e0/36e09ed160da32adb5b246d71ecf9051e278a59d5d38ec1ba956fcc24de52f37.jpg?f=444487320121389056.png",
            )

            fun ViewPager2.updateHeight(newHeight: Int) {
                post {
                    val startHeight = height
                    ValueAnimator.ofInt(startHeight, newHeight).apply {
                        duration = 100
                        interpolator = OvershootInterpolator(1.1f)
                        addUpdateListener { anim ->
                            val value = anim.animatedValue as Int
                            layoutParams = layoutParams.apply { height = value }
                            requestLayout()
                        }
                    }.start()
                }
            }

            val mediaPagerAdapter = MediaPagerAdapter(imageUrls) { newHeight ->
                mediaPager.updateHeight(newHeight)
            }

            mediaPager.apply {
                adapter = mediaPagerAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        mediaPagerAdapter.getHeightForPosition(position)?.let { height ->
                            updateHeight(height)
                        }
                    }
                })
            }

            textDescription.text = "${post.title}\n\n${post.substring}"

            buttonUsername.text = post.user

            buttonPfp.load("https://img.kemono.su/icons/${post.service}/${post.user}")
            
            buttonPfp.setOnClickListener { onProfileClick(post) }

            buttonMore.setOnClickListener { onMoreOptionsClick(post) }

            toggleLike.setOnCheckedChangeListener { _, isChecked -> onLikeToggled(post, isChecked) }

            toggleBookmark.setOnCheckedChangeListener { _, isChecked -> onBookmarkToggled(post, isChecked) }

            buttonShare.setOnClickListener { onShareClick(post) }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}