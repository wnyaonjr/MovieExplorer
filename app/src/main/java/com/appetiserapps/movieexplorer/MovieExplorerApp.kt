package com.appetiserapps.movieexplorer

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application definition, contains starting of logger when build type is debug
 */
@HiltAndroidApp
class MovieExplorerApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}