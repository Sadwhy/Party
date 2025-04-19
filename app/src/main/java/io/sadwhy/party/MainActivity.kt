package io.sadwhy.party

import android.os.Bundle
import android.graphics.Color.TRANSPARENT
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import io.sadwhy.party.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge(
            SystemBarStyle.auto(TRANSPARENT, TRANSPARENT),
            SystemBarStyle.dark(TRANSPARENT)
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        val toolbar = binding.topAppBar
        setSupportActionBar(toolbar)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    toolbar.title = "Home"
                    toolbar.navigationIcon = null // Remove nav icon
                    toolbar.menu.clear()
                }
                R.id.searchFragment -> {
                    toolbar.title = "Search"
                    toolbar.setNavigationIcon(R.drawable.ic_back)
                    toolbar.menu.clear()
                }
                R.id.libraryFragment -> {
                    toolbar.title = "Library"
                    toolbar.navigationIcon = null
                    toolbar.menu.clear()
                }
                else -> {
                    toolbar.title = ""
                    toolbar.navigationIcon = null
                    toolbar.menu.clear()
                }
            }
        }
        
        
    }
}
