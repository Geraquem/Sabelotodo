package com.mmfsin.sabelotodo.di

import com.mmfsin.sabelotodo.data.repository.DashboardRepository
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DashboardRepositoryModule {
    @Binds
    fun bind(repository: DashboardRepository): IDashboardRepository
}