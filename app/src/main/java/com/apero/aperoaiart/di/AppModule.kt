package com.apero.aperoaiart.di

import com.apero.aperoaiart.ui.screen.pickphoto.PickPhotoViewModel
import com.apero.aperoaiart.ui.screen.result.ResultViewModel
import com.apero.aperoaiart.ui.screen.style.StyleViewModel
import com.apero.aperoaiart.utils.PermissionUtil
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule = module {
}

val viewModelModule = module {
    viewModelOf(::StyleViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::PickPhotoViewModel)
}

val permissionModule = module {
    single { PermissionUtil(androidContext()) }
}

val appModule: List<Module> = buildList {
    add(viewModelModule)
    add(repositoryModule)
    add(permissionModule)
}