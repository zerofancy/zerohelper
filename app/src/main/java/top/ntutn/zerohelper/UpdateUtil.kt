package top.ntutn.zerohelper

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*

object UpdateUtil {
    fun checkUpdate(scope: CoroutineScope, callback: (Pair<Boolean, ApkElement?>) -> Unit) {
        scope.launch {
            val apkMetaData = withContext(Dispatchers.IO) {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://github.com/")
                    .build()
                val service = retrofit.create(CheckUpdateApi::class.java)
                service.getApkMetaData()
            }
            val res = apkMetaData.elements.firstOrNull()?.let {
                (it.versionCode > BuildConfig.VERSION_CODE) to it
            } ?: kotlin.run {
                false to null
            }
            callback.invoke(res)
        }
    }

    fun download(context: Context, url: String) {
        val downloadManager =
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager?)
                ?: kotlin.run {
                    Toast.makeText(context, "下载失败，系统下载服务异常", Toast.LENGTH_SHORT).show()
                    return
                }
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedOverRoaming(true)
            setTitle("正在更新")
            setDescription("Zero Helper正在下载新版本")
            setVisibleInDownloadsUi(true)
        }
        val fileName = UUID.randomUUID().toString() + ".apk"
        val targetFile =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        request.setDestinationUri(Uri.fromFile(targetFile))
        downloadManager.enqueue(request)
    }
}