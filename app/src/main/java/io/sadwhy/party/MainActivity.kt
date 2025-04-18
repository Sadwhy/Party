package io.sadwhy.party

import android.os.Bundle
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import io.sadwhy.party.databinding.ActivityMainBinding

class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
	val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
    }
}
