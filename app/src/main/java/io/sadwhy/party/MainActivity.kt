package io.sadwhy.party

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.sadwhy.party.core.debug.console.FloatingConsole
import io.sadwhy.party.core.theme.AppTheme
import io.party.sadwhy.core.navigation.Navigator

/**
 * Small holder so NavHostController can be accessed anywhere without threading it through parameters.
 * Make sure setNavController(...) is called *before* any call to getNavController().
 */
object NavControllerHolder {
    private var navController: NavHostController? = null

    fun setNavController(controller: NavHostController) {
        navController = controller
    }

    fun getNavController(): NavHostController {
        return navController
            ?: throw IllegalStateException("NavController not set")
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavControllerHolder.setNavController(navController)

                // create a global Navigator if you want (optional)
                remember(navController) { Navigator(navController) }

                FloatingConsole {
                    MainScreen()
                }
            }
        }
    }
}