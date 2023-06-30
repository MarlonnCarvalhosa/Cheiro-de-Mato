package com.marlonncarvalhosa.cheirodemato.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.marlonncarvalhosa.cheirodemato.di.listModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CheiroDeMatoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
        startKoin{
            androidContext(this@CheiroDeMatoApplication)
            modules(listModules)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: Context? = null
            private set
    }
}