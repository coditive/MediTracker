package com.syrous.meditracker

import android.app.Application

class MediTrackerApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent(this)
    }
}