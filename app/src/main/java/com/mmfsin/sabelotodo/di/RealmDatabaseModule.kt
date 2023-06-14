package com.mmfsin.sabelotodo.di

import com.mmfsin.sabelotodo.data.database.RealmDatabase
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
object RealmDatabaseModule {

    @RealmModule(library = true, allClasses = true)
    class CoreModule

    @Provides
    fun provideRealmDatabase(): IRealmDatabase {
        return RealmDatabase(
            RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .modules(CoreModule())
                .build()
        )
    }
}