package top.ntutn.zerohelper

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import top.ntutn.zerohelper.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindView()
    }

    private fun bindView() {
        binding.demoItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.demoItemRecyclerView.adapter = MainListAdapter(mutableListOf<MainListAdapter.Item>().apply {
            title("第一个标题")
            repeat(10) {
                item("第${it}个项目")
            }
            title("第二个标题")
            repeat(10) {
                item("第${it}个项目")
            }
            title("第三个标题")
            repeat(10) {
                item("第${it}个项目")
            }
        })
        title = "Demo列表"
    }
}