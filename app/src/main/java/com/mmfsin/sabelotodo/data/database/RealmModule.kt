package com.mmfsin.sabelotodo.data.database

import io.realm.RealmConfiguration

object RealmModule {
    fun realmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
    }
}