package io.sadwhy.party.ui.main.library

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import io.sadwhy.party.R
import io.sadwhy.party.databinding.LibraryFragmentBinding
import io.sadwhy.party.media.adapter.PostAdapter
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared
import io.sadwhy.party.utils.log
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        setupRecyclerView()
        observeViewModel()
        
        // Explicitly fetch posts when the fragment is created
        viewModel.fetchPosts()
    }
    
    private fun setupRecyclerView() {
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
    }
    
    private fun observeViewModel() {
        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    binding.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
                    log("Loading state: $isLoading")
                }
            }
        }
        
        // Observe posts
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collectLatest { posts ->
                    log("Posts received in fragment: ${posts.size}")
                    Toast.makeText(requireContext(), "Posts received: ${posts.size}", Toast.LENGTH_SHORT).show()
                    postAdapter.submitList(posts)
                    
                    // Make the recycler view visible if we have posts
                    if (posts.isNotEmpty()) {
                        binding.postRecyclerView.visibility = View.VISIBLE
                        binding.emptyStateView?.visibility = View.GONE
                    } else {
                        binding.postRecyclerView.visibility = View.VISIBLE // Still show even if empty
                        binding.emptyStateView?.visibility = View.VISIBLE
                    }
                }
            }
        }
        
        // Observe errors
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { errorMsg ->
                    errorMsg?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        log("Error observed: $it")
                    }
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh data when returning to the fragment
        viewModel.fetchPosts()
    }
}