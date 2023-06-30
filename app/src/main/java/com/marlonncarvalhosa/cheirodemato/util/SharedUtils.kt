package com.marlonncarvalhosa.cheirodemato.util

import android.content.Context

object SharedUtils {
    private var mInstance: SharedPreferences? = null

    private val sessionPreferences: android.content.SharedPreferences
        get() {
            val ctx = CheiroDeMatoApplication.instance
            return ctx!!.getSharedPreferences("SESSION_PREFERENCES", Context.MODE_PRIVATE)
        }

    fun setValueInSharedPreferences(key: String, value: String) {
        sessionPreferences.edit().putString(key,value).apply()
    }

    fun getValueInSharedPreferences(key: String): String {
        // Retorna vazio caso nao encontre uma chave correspondente.
        return sessionPreferences.getString(key,"").toString()
    }

    fun removeValueInSharedPreferences(key: String) {
        sessionPreferences.edit()?.remove(key)?.apply()
    }

    fun getInstance(context: Context): SharedPreferences {
        if (mInstance == null)
            mInstance = SharedPreferences(context)
        return mInstance as SharedPreferences
    }
}