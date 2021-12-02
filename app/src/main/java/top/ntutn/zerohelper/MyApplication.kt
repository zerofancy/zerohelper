package top.ntutn.zerohelper

import android.app.Application
import top.ntutn.zerohelper.util.ApplicationUtil
import top.ntutn.zerohelper.util.LooperProxyUtil

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ApplicationUtil.init(applicationContext)
        LooperProxyUtil.init()
    }
}