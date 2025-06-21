package com.duyhellowolrd.ai_art_service.data

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun observeNetworkStatus(): Flow<Boolean>
    fun isConnectedNow(): Boolean
}