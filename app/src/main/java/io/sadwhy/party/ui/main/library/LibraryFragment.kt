package io.sadwhy.party.ui.main.library

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.sadwhy.party.R
import io.sadwhy.party.databinding.LibraryFragmentBinding
import io.sadwhy.party.media.adapter.PostAdapter
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared

class LibraryFragment : Fragment(R.layout.library_fragment) {
    private var binding: LibraryFragmentBinding by autoCleared()
    private val viewModel: LibraryViewModel by viewModels()

    private lateinit var postAdapter: PostAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = LibraryFragmentBinding.bind(view)

        // Initialize adapter with lambda functions
        postAdapter = PostAdapter(
            onProfileClick = { post ->
                Toast.makeText(requireContext(), "Profile: ${post.user}", Toast.LENGTH_SHORT).show()
            },
            onMoreOptionsClick = { post ->
                Toast.makeText(requireContext(), "More options for: ${post.user}", Toast.LENGTH_SHORT).show()
            },
            onLikeToggled = { post, isLiked ->
                Toast.makeText(requireContext(), "Liked ${post.user}: $isLiked", Toast.LENGTH_SHORT).show()
            },
            onBookmarkToggled = { post, isBookmarked ->
                Toast.makeText(requireContext(), "Bookmarked ${post.user}: $isBookmarked", Toast.LENGTH_SHORT).show()
            },
            onShareClick = { post ->
                Toast.makeText(requireContext(), "Sharing ${post.user}'s post", Toast.LENGTH_SHORT).show()
            }
        )

        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        viewModel.fetchPosts()

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
        }
    }
}