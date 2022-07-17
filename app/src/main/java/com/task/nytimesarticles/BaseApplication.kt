
package com.task.nytimesarticles

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.task.nytimesarticles.common.isNight
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val uiMode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(uiMode)
    }
}
