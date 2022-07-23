package top.ntutn.zerohelper.util

import top.ntutn.zerohelper.container.CrashReportActivity

object LooperProxyUtil : Thread.UncaughtExceptionHandler {

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        CrashReportActivity.showCrashInfo("thread: ${t.name}\n\n${e.stackTraceToString()}")
    }
}