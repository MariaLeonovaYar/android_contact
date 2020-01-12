package ru.tensor.sbis.recyclerview

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

internal class RecyclerViewApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}