package com.mmfsin.sabelotodo.data.database

import com.mmfsin.sabelotodo.domain.interfaces.IRealmDatabase
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isValid

class RealmDatabase(private val realmConfiguration: RealmConfiguration) : IRealmDatabase {

    private fun getRealm(): Realm {
        return try {
            Realm.getInstance(realmConfiguration)
        } catch (e: IllegalArgumentException) {
            deleteAllData()
            Realm.getInstance(realmConfiguration)
        }
    }

    override fun deleteAllData() {
        val realm = getRealm()
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
        realm.close()
    }

    override fun <O : RealmResults<I>, I : RealmModel> getObjectsFromRealm(action: Realm.() -> O): List<I> {
        val realm = getRealm()
        val results = action(realm)
        val list = results.map { realm.copyFromRealm(it) }
        realm.close()
        return list
    }

    override fun <T : RealmModel> addObject(action: () -> T) {
        val realm = getRealm()
        val realmModel = action()
        realm.beginTransaction()
        realm.insertOrUpdate(realmModel)
        realm.commitTransaction()
        realm.close()
    }

    override fun <T : RealmModel> deleteObject(action: Realm.() -> T, id: String) {
        val realm = getRealm()
        val realmModel = action(realm)
        realm.beginTransaction()
        if (realmModel.isValid()) {
            val obj = realm.where(realmModel.javaClass).equalTo("id", id).findFirst()
            if (obj != null) {
                obj.deleteFromRealm()
                realm.commitTransaction()
                realm.close()
            } else realm.cancelTransaction()
        } else realm.cancelTransaction()
    }
}