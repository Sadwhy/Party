package io.sadwhy.party.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import io.sadwhy.party.R
import io.sadwhy.party.databinding.HomeFragmentBinding
import io.sadwhy.party.utils.AutoClearedValue.Companion.autoCleared

class HomeFragment : Fragment(R.layout.home_fragment) {
    private var binding: HomeFragmentBinding by autoCleared()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)
    
        binding.homeCompose.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.homeCompose.setContent {
            HomeScreen()
        }
    }
}
