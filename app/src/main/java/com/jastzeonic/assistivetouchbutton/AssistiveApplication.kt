package com.jastzeonic.assistivetouchbutton

import android.app.Application
import android.content.Intent

/**
 * Created by Jast Lai on 2017/6/15.
 */

class AssistiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startService(Intent(this, TopFloatService::class.java))

    }
}
