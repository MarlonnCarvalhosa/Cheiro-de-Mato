package com.marlonncarvalhosa.cheirodemato.util

import android.content.Context

class SharedPreferences(val context: Context) {

    private val sessionPreferences: android.content.SharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    fun setValueInSharedPreferences(key: String, value: String) {
        sessionPreferences.edit()?.putString(key, value)?.apply()
    }

    fun getValueInSharedPreferences(key: String): String {
        // Retorna vazio caso nao encontre uma chave correspondente.
        return sessionPreferences.getString(key, "")?:""
    }

    fun removeValueInSharedPreferences(key: String) {
        sessionPreferences.edit()?.remove(key)?.apply()
    }

    companion object {

        private var mInstance: SharedPreferences? = null

        fun getInstance(context: Context): SharedPreferences {
            if (mInstance == null)
                mInstance = SharedPreferences(context)
            return mInstance as SharedPreferences
        }
    }
}