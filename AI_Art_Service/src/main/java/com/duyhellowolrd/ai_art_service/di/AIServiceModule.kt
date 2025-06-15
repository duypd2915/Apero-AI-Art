package com.duyhellowolrd.ai_art_service.di

import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import com.duyhellowolrd.ai_art_service.data.impl.AiArtRepositoryImpl
import com.duyhellowolrd.ai_art_service.network.ApiClient
import com.duyhellowolrd.ai_art_service.network.service.AIStyleService
import com.duyhellowolrd.ai_art_service.network.service.AiArtService
import com.duyhellowolrd.ai_art_service.network.service.TimeStampService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val serviceModule = module {
    single<AiArtService> { ApiClient.getAiArtService() }
    single<TimeStampService> { ApiClient.getTimeStampService() }
    single<AIStyleService> { ApiClient.getStyleService() }
}

internal val repositoryModule = module {
    single<AiArtRepository> { AiArtRepositoryImpl(androidContext(), get(), get(), get()) }
    single { PhotoRepository(androidContext()) }
}

val aiServiceModule = listOf(
    serviceModule,
    repositoryModule
)