package com.apero.aperoaiart.di

import com.apero.aperoaiart.ui.screen.style.StyleViewModel
import com.apero.aperoaiart.utils.PermissionUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationScope = module {
    single(named("coroutine_app")) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> })
    }
}

val repositoryModule = module {
}

val viewModelModule = module {
    viewModelOf(::StyleViewModel)
}

val permissionModule = module {
    single { PermissionUtil(androidContext()) }
}

val appModule: List<Module> = buildList {
    add(applicationScope)
    add(viewModelModule)
    add(repositoryModule)
    add(permissionModule)
}