package top.ntutn.zerohelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import top.ntutn.zerohelper.databinding.ActivityTestViewPager2Binding

class TestViewPager2Activity : BaseActivity() {
    companion object {
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, TestViewPager2Activity::class.java))
        }
    }

    private lateinit var binding: ActivityTestViewPager2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestViewPager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        })
    }
}

class ViewPager2Adapter : RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>() {
    class ViewHolder(viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}