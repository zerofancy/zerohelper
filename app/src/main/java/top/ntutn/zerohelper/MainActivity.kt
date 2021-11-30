package top.ntutn.zerohelper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import top.ntutn.zerohelper.databinding.ActivityMainBinding
import android.app.DownloadManager

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.database.Cursor
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.haveUpdate.observe(this) {
            if (it.first) {
                AlertDialog.Builder(this)
                    .setTitle("发现新版本")
                    .setMessage("本地${BuildConfig.VERSION_CODE}, 远端${it.second!!.versionCode}。是否更新？")
                    .setPositiveButton("是") { _, _ ->
                        Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show()
                        val targetFileName =
                            it.second!!.outputFile.removeSuffix(".apk").plus("-signed.apk")
                        val targetUrl =
                            "https://github.com/zerofancy/zerohelper/releases/download/latest/$targetFileName"
                        viewModel.download(this, targetUrl)
                    }
                    .setNegativeButton("否") { _, _ ->
                        Toast.makeText(this, "取消更新", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.downloadId.observe(this) {
            it?:return@observe
            registerReceiver(receiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        }

        binding.test.setOnClickListener {
            viewModel.checkForUpdate()
        }
        binding.crashButton.setOnClickListener {
            thread {
                throw RuntimeException("主动Crash")
            }.start()
        }
        binding.killButton.setOnClickListener {
            ApplicationUtil.restart()
        }

        UpdateUtil.init(this)
    }

    //广播监听下载的各个状态
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkStatus()
        }
    }

    //检查下载状态
    private fun checkStatus() {
        val query = DownloadManager.Query()
        //通过下载的id查找
        query.setFilterById(viewModel.downloadId.value!!)
        val cursor: Cursor = UpdateUtil.downloadManager?.query(query)?:return
        if (cursor.moveToFirst()) {
            val column = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS).takeIf { it>=0 }?:return
            val status: Int = cursor.getInt(column)
            when (status) {
                DownloadManager.STATUS_PAUSED -> {}
                DownloadManager.STATUS_PENDING -> {}
                DownloadManager.STATUS_RUNNING -> {}
                DownloadManager.STATUS_SUCCESSFUL -> {
                    //下载完成安装APK
                    viewModel.installApk(this)
                    Toast.makeText(this, "Download succeed", Toast.LENGTH_SHORT).show()
                    cursor.close()
                }
                DownloadManager.STATUS_FAILED -> {
                    Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    this.unregisterReceiver(receiver)
                }
            }
        }
    }
}