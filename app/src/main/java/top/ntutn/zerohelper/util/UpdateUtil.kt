package top.ntutn.zerohelper.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import android.content.Intent

import androidx.core.content.FileProvider

import android.os.Build
import top.ntutn.zerohelper.ApkElement
import top.ntutn.zerohelper.BuildConfig
import top.ntutn.zerohelper.CheckUpdateApi

object UpdateUtil {
    private const val PROVIDER_AUTHORITIES_DEBUG = "top.ntutn.zerohelper.debug.fileprovider"
    private const val PROVIDER_AUTHORITIES_RELEASE = "top.ntutn.zerohelper.fileprovider"

    var downloadManager: DownloadManager? = null

    fun init(context: Context) {
        downloadManager =
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager?)
    }

    fun destroy() {
        downloadManager = null
    }

    fun checkUpdate(scope: CoroutineScope, callback: (Pair<Boolean, ApkElement?>) -> Unit) {
        scope.launch {
            val apkMetaData = try {
                withContext(Dispatchers.IO) {
                    val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://github.com/")
                        .build()
                    val service = retrofit.create(CheckUpdateApi::class.java)
                    service.getApkMetaData()
                }
            } catch (e: Exception) {
                null
            }
            val res = apkMetaData?.elements?.firstOrNull()?.let {
                (it.versionCode > BuildConfig.VERSION_CODE) to it
            } ?: kotlin.run {
                false to null
            }
            callback.invoke(res)
        }
    }

    fun download(context: Context, url: String, targetFile: File): Long? {
        downloadManager ?: kotlin.run {
            Toast.makeText(context, "下载失败，系统下载服务异常", Toast.LENGTH_SHORT).show()
            return null
        }
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedOverRoaming(true)
            setTitle("正在更新")
            setDescription("Zero Helper正在下载新版本")
            setVisibleInDownloadsUi(true)
        }
        request.setDestinationUri(Uri.fromFile(targetFile))
        return downloadManager?.enqueue(request)
    }

    //修改文件权限
    private fun setPermission(absolutePath: String) {
        val command = "chmod 777 $absolutePath"
        val runtime = Runtime.getRuntime()
        try {
            runtime.exec(command)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun installAPK(context: Context, savedFile: File) {
        val pathstr: String = savedFile.absolutePath
        setPermission(pathstr)
        val intent = Intent(Intent.ACTION_VIEW)
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //Android 7.0以上要使用FileProvider
        if (Build.VERSION.SDK_INT >= 24) {
            val file = File(pathstr)
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            val apkUri =
                FileProvider.getUriForFile(
                    context,
                    if (BuildConfig.DEBUG) {
                        PROVIDER_AUTHORITIES_DEBUG
                    } else {
                        PROVIDER_AUTHORITIES_RELEASE
                    }, file
                )
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(
                Uri.fromFile(File(Environment.DIRECTORY_DOWNLOADS, savedFile.name)),
                "application/vnd.android.package-archive"
            )
        }
        context.startActivity(intent)
    }
}