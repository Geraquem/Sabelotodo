package com.mmfsin.sabelotodo.di

import com.mmfsin.sabelotodo.data.repository.CategoryRepository
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CategoryRepositoryModule {
    @Binds
    fun bind(repository: CategoryRepository): ICategoryRepository
}