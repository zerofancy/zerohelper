package top.ntutn.zerohelper.util

import android.content.Context
import android.content.Intent
import android.os.Process
import top.ntutn.zerohelper.container.SplashActivity
import kotlin.system.exitProcess


object ApplicationUtil {
    lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    fun restart() {
        val intent = Intent(
            applicationContext,
            SplashActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}