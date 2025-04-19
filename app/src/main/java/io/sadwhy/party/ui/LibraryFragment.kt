package io.sadwhy.party.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil3.load
import io.getstream.photoview.dialog.PhotoViewDialog
import io.sadwhy.party.databinding.LibraryFragmentBinding

class LibraryFragment : Fragment() {
    private var _binding: LibraryFragmentBinding? = null
    private val binding get() = _binding!!

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

        val imageUrls =
            listOf(
                "https://d.fixupx.com/DarkOddCon/status/1912812028210913632",
                "https://d.fixupx.com/lazycataw/status/1912738980879016263",
                "https://coomer.su/data/07/cf/07cf242a44eef7f1d968241638159f971c056d659150304916584e9ddf04f097.jpg?f=444487380599058432.png",
                "https://coomer.su/data/04/4a/044a45a2757fd6a77a6df8ae0c23ddf4a7868dde405bfe137e6dbaa3289b6fef.jpg?f=444487209794416641.png",
                "https://coomer.su/data/36/e0/36e09ed160da32adb5b246d71ecf9051e278a59d5d38ec1ba956fcc24de52f37.jpg?f=444487320121389056.png"
            )

        binding.toggleLike.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.postImage.load(imageUrls[0])
                binding.postImageA.load(imageUrls[1])
            }
        }

        binding.postImage.setOnClickListener {
            PhotoViewDialog
                .Builder(
                    context = requireContext(),
                    images = imageUrls,
                ) { imageView, url ->
                    imageView.load(url)
                }.build()
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
