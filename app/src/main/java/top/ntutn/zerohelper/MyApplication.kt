package top.ntutn.zerohelper

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ApplicationUtil.init(applicationContext)
        LooperProxyUtil.init()
    }
}