package com.mmfsin.sabelotodo.data.database

import com.mmfsin.sabelotodo.data.database.RealmModule.realmConfiguration
import com.mmfsin.sabelotodo.domain.models.DataDTO
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isValid

class RealmDatabase {

    private val realmConfiguration by lazy { realmConfiguration() }

    private fun getRealm(): Realm {
        return try {
            Realm.getInstance(realmConfiguration)
        } catch (e: IllegalArgumentException) {
            deleteAllData()
            Realm.getInstance(realmConfiguration)
        }
    }

    private fun deleteAllData() {
        val realm = getRealm()
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
        realm.close()
    }

    fun <O : RealmResults<I>, I : RealmModel> getObjectsFromRealm(action: Realm.() -> O): List<I> {
        val realm = getRealm()
        val results = action(realm)
        val list = results.map { realm.copyFromRealm(it) }
        realm.close()
        return list
    }

    fun <T : RealmModel> addObject(action: () -> T): Boolean {
        return try {
            val realm = getRealm()
            val realmModel = action()
            realm.beginTransaction()
            realm.insertOrUpdate(realmModel)
            realm.commitTransaction()
            realm.close()
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    fun <T : RealmModel> deleteObject(action: Realm.() -> T, id: String): Boolean {
        val realm = getRealm()
        val realmModel = action(realm)
        realm.beginTransaction()
        return try {
            if (realmModel.isValid()) {
                val obj = realm.where(realmModel.javaClass).equalTo("id", id).findFirst()
                if (obj != null) {
                    obj.deleteFromRealm()
                    realm.commitTransaction()
                    realm.close()
                    true
                } else {
                    realm.cancelTransaction()
                    false
                }
            } else {
                realm.cancelTransaction()
                false
            }

        } catch (e: Exception) {
            realm.cancelTransaction()
            false
        }
    }


    fun deleteData() {
        val realm = getRealm()
        realm.beginTransaction()
        realm.delete(DataDTO::class.java)
        realm.commitTransaction()
        realm.close()
    }
}