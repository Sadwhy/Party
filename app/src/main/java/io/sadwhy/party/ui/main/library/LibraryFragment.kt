package io.sadwhy.party.ui.main.library

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.sadwhy.party.R
import io.sadwhy.party.databinding.LibraryFragmentBinding
import io.sadwhy.party.media.adapter.PostAdapter
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared

class LibraryFragment : Fragment(R.layout.library_fragment) {
    private var binding: LibraryFragmentBinding by autoCleared()
    private val postAdapter = PostAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = LibraryFragmentBinding.bind(view)

        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        val dummyPosts =
            listOf(
                Post(user = "UserA", content = "Sample post A"),
                Post(user = "UserB", content = "Sample post B"),
            )

        postAdapter.submitList(dummyPosts)
    }
}
