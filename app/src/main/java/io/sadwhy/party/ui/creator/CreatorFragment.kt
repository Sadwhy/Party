package io.sadwhy.party.ui.creator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil3.load
import io.sadwhy.party.R
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.databinding.CreatorFragmentBinding
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared
import io.sadwhy.party.utils.Serializer.getSerialized
import io.sadwhy.party.utils.Serializer.putSerialized

class CreatorFragment : Fragment(R.layout.creator_fragment) {
    companion object {
        fun getBundle(post: Post) = Bundle().apply {
            putSerialized("post", post)
        }
    }

    private val args by lazy { requireArguments() }
    private val post by lazy { args.getSerialized<Post>("post")!! }

    private var binding: CreatorFragmentBinding by autoCleared()
    private val viewModel: CreatorViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatorFragmentBinding.bind(view)

        binding.username.text = post.user
        
        binding.collapsingToolbar.title = post.user
        
        listOf("Posts", "Media", "About").forEach {
            binding.tabLayout.addTab(tabLayout.newTab().setText(it))
        }
        
        binding.profileImage.load("https://img.kemono.su/icons/${post.service}/${post.user}")
        
        binding.bannerImage.load("https://img.kemono.su/banners/${post.service}/${post.user}")
    }
}