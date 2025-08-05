package io.sadwhy.party

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import io.sadwhy.party.core.debug.console.FloatingConsole
import io.sadwhy.party.core.theme.AppTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            AppTheme {
                FloatingConsole { MainScreen() }
            }
        }
    }
}
