package top.ntutn.zerohelper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Process
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import top.ntutn.zerohelper.databinding.ActivityCrashReportBinding
import top.ntutn.zerohelper.util.ApplicationUtil
import kotlin.system.exitProcess

class CrashReportActivity : AppCompatActivity() {
    companion object {
        private const val KEY_CRASH_INFO = "key_crash_info"

        fun showCrashInfo(info: String) {
            val intent = Intent(
                ApplicationUtil.applicationContext,
                CrashReportActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(KEY_CRASH_INFO, info)

            ApplicationUtil.applicationContext.startActivity(intent)
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
    }

    private lateinit var binding: ActivityCrashReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "被玩坏了～"

        val crashInfo = intent.getStringExtra(KEY_CRASH_INFO)
        binding.crashStackTextView.text = crashInfo
        binding.copyToClipboardButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager?: kotlin.run {
                Toast.makeText(this, "复制失败", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val clip = ClipData.newPlainText("crash", crashInfo)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show()
        }
        binding.sendToDeveloperButton.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                .setView(editText)
                .setTitle("输入邮箱地址")
                .setMessage("输入接收者的邮件地址，分号分隔")
                .setPositiveButton("发送") { _,_->
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${editText.text}")).apply {
                        putExtra(Intent.EXTRA_SUBJECT, "crash报告")
                        putExtra(Intent.EXTRA_TEXT, crashInfo)
                    }
                    startActivity(Intent.createChooser(intent, "通过邮件发送crash报告"))
                }
                .setNegativeButton("取消") { _,_ ->
                    // do nothing
                }
                .create().show()
        }
        binding.restartButton.setOnClickListener {
            ApplicationUtil.restart()
        }
    }
}