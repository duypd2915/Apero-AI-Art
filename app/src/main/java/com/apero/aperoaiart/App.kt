package com.apero.aperoaiart

import android.app.Application
import com.apero.aperoaiart.di.DIContainer
import com.apero.aperoaiart.utils.Constants
import com.duyhellowolrd.ai_art_service.AiArtServiceEntry

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DIContainer.init(this)
        AiArtServiceEntry.init(
            apiKey = Constants.API_KEY,
            appName = Constants.APP_NAME,
            bundleId = Constants.BUNDLE_ID,
        )
    }
}