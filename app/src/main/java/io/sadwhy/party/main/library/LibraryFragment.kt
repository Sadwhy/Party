package io.sadwhy.party.main.library

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.sadwhy.party.R
import io.sadwhy.party.databinding.LibraryFragmentBinding
import io.sadwhy.party.media.adapter.PostAdapter
import io.sadwhy.party.media.model.Post
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared
import io.sadwhy.party.utils.function.setupPostList

class LibraryFragment : Fragment(R.layout.library_fragment) {
    private var binding: LibraryFragmentBinding by autoCleared()
    private val postAdapter = PostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LibraryFragmentBinding.bind(view)

        // Set up RecyclerView using extension function
        binding.postRecyclerView.setupPostList(
            context = requireContext(),
            postAdapter = postAdapter,
            posts = listOf(
                Post(username = "UserA", description = "Sample post A"),  
                Post(username = "UserB", description = "Sample post B")
            )
        )

    }
}