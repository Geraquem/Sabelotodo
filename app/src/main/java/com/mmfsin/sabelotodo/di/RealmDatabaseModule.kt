package com.mmfsin.sabelotodo.di

import com.mmfsin.sabelotodo.data.database.RealmDatabase
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.data.models.LoserImagesDTO
import com.mmfsin.sabelotodo.data.models.UserRecordDTO
import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
object RealmDatabaseModule {

    @Provides
    fun provideRealmDatabase(): IRealmDatabase {
        val config = RealmConfiguration.create(
            schema = setOf(
                CategoryDTO::class,
                LoserImagesDTO::class,
                UserRecordDTO::class
            )
        )

        val realm = Realm.open(config)
        return RealmDatabase(realm)
    }
}