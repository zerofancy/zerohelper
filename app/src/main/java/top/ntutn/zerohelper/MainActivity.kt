package top.ntutn.zerohelper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import top.ntutn.zerohelper.databinding.ActivityMainBinding

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

        binding.test.setOnClickListener {
            viewModel.checkForUpdate()
        }
    }
}