package io.sadwhy.party

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.allowHardware
import coil3.request.crossfade
import com.google.android.material.color.DynamicColors
import io.sadwhy.party.network.OkHttp

class Party :
    Application(),
    SingletonImageLoader.Factory {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        setupLogging()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    private fun setupLogging() {
        try {
            // Create or truncate main log file to avoid it growing too large
            val logFile = File(appContext.filesDir, "app_log.txt")
            if (logFile.exists() && logFile.length() > 5 * 1024 * 1024) { // 5MB limit
                logFile.delete()
                logFile.createNewFile()
            }
            
            if (!logFile.exists()) {
                logFile.createNewFile()
            }
        } catch (e: Exception) {
            Log.e("PartyApp", "Failed to initialize logging system", e)
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader
            .Builder(context)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { OkHttp.client }
                    )
                )
            }
            .memoryCache {
                MemoryCache
                    .Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache
                    .Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(1024 * 1024 * 100) // 100MB
                    .build()
            }
            .allowHardware(false)
            .crossfade(true)
            .build()
}