package top.ntutn.zerohelper.container

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import top.ntutn.zerohelper.MainListAdapter
import top.ntutn.zerohelper.base.BaseActivity
import top.ntutn.zerohelper.databinding.ActivitySplashBinding
import top.ntutn.zerohelper.item
import top.ntutn.zerohelper.title
import java.lang.RuntimeException

class MainActivity : BaseActivity() {
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
            title("基础组件")
            item("ViewPager2") {
                TestViewPager2Activity.actionStart(this@MainActivity)
            }
            item("RecyclerView局部刷新") {
                RecyclerViewActivity.actionStart(this@MainActivity)
            }
            title("自定义View")
            item("电池") {
                TestBatteryViewActivity.actionStart(this@MainActivity)
            }
            title("基建")
            item("崩溃报告") {
                throw RuntimeException("触发crash")
            }
            title("其他")
            item("关于") {
                AboutActivity.actionStart(this@MainActivity)
            }
        })
        title = "Demo列表"
    }
}