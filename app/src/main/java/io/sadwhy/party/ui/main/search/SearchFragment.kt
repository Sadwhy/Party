package io.sadwhy.party.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import io.sadwhy.party.R
import io.sadwhy.party.databinding.SearchFragmentBinding
import io.sadwhy.party.ui.theme.AppTheme
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared

class SearchFragment : Fragment(R.layout.search_fragment) {
    private var binding: SearchFragmentBinding by autoCleared()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SearchFragmentBinding.bind(view)
    
        binding.searchCompose.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.searchCompose.setContent {
            AppTheme {
                SearchScreen("Search", "Compose Search Screen")
            }
        }
    }
}
