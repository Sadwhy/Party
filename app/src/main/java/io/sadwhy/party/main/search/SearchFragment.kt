package io.sadwhy.party.main.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.sadwhy.party.R
import io.sadwhy.party.databinding.SearchFragmentBinding
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared

class SearchFragment : Fragment(R.layout.search_fragment) {
    private var binding: SearchFragmentBinding by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: binding usable
    }
}
