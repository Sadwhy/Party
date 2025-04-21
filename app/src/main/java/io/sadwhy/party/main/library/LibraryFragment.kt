package io.sadwhy.party.main.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.sadwhy.party.databinding.LibraryFragmentBinding
import io.sadwhy.party.media.adapter.PostAdapter
import io.sadwhy.party.media.model.Post

class LibraryFragment : Fragment() {
    private var _binding: LibraryFragmentBinding? = null
    private val binding get() = _binding!!

    private val postAdapter = PostAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LibraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        val dummyPosts =
            listOf(
                Post(username = "UserA", description = "Sample post A"),
                Post(username = "UserB", description = "Sample post B"),
            )

        postAdapter.submitList(dummyPosts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
