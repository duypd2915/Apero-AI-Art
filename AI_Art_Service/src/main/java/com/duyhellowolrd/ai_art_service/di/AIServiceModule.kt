package com.duyhellowolrd.ai_art_service.di

import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.NetworkRepository
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import com.duyhellowolrd.ai_art_service.data.impl.AiArtRepositoryImpl
import com.duyhellowolrd.ai_art_service.data.impl.NetworkRepositoryImpl
import com.duyhellowolrd.ai_art_service.network.ApiClient
import com.duyhellowolrd.ai_art_service.network.service.AIStyleService
import com.duyhellowolrd.ai_art_service.network.service.AiArtService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val serviceModule = module {
    single<AiArtService> { ApiClient.getAiArtService() }
    single<AIStyleService> { ApiClient.getStyleService() }
}

internal val repositoryModule = module {
    single<AiArtRepository> { AiArtRepositoryImpl(androidContext(), get(), get(), get()) }
    single { PhotoRepository(androidContext()) }
    single<NetworkRepository> { NetworkRepositoryImpl(androidContext()) }
}

val aiServiceModule = listOf(
    serviceModule,
    repositoryModule
)