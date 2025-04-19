package io.sadwhy.party.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil3.load
import coil3.clear
import io.getstream.photoview.dialog.PhotoViewDialog
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

      val imageUrls = listOf(
      "https://d.fixupx.com/DarkOddCon/status/1912812028210913632",
      "https://d.fixupx.com/lazycataw/status/1912738980879016263",
      "https://d.fixupx.com/FeintHeart721/status/1913544604726837686",
      "https://d.fixupx.com/panpianoatelier/status/1913206722485002329"
      )


    binding.toggleLike.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        binding.postImage.load(imageUrls[0])
        binding.postImageA.load(imageUrls[1])
      } else {
        binding.postImage.clear()
        binding.postImageA.clear()
      }
    }


    binding.postImage.setOnClickListener {
      PhotoViewDialog.Builder(
        context = requireContext(),
        images  = imageUrls
      ) { imageView: ImageView, url: String ->
        imageView.load(url)
      }
      .build()
      .show()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}