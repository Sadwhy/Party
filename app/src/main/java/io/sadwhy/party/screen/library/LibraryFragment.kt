package io.sadwhy.party.screen.library

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
import io.sadwhy.party.legacy.media.adapter.PostAdapter
import io.sadwhy.party.legacy.common.FragmentUtils.openFragment
import io.sadwhy.party.screen.creator.CreatorFragment
import io.sadwhy.party.legacy.utils.AutoClearedValue.Companion.autoCleared
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

        postAdapter = PostAdapter(
            onProfileClick = { post ->
                //requireActivity().openFragment<CreatorFragment>(
                //    null,
                //    CreatorFragment.getBundle(post)
                //)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collectLatest { recent ->
                    val postList = recent?.posts.orEmpty()
                    postAdapter.submitList(postList)
                }
            }
        }
    }
}