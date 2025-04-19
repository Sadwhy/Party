package io.sadwhy.party.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil3.load
import io.sadwhy.party.databinding.LibraryFragmentBinding

class LibraryFragment : Fragment() {

    private var _binding: LibraryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LibraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.toggleLike.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.postImage.load("https://d.fixupx.com/DarkOddCon/status/1912812028210913632")
                binding.postImageA.load("https://d.fixupx.com/lazycataw/status/1912738980879016263")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
