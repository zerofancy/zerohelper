package top.ntutn.zerohelper.container

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import top.ntutn.zerohelper.base.BaseActivity
import top.ntutn.zerohelper.databinding.ActivityTestViewPager2Binding
import top.ntutn.zerohelper.databinding.LayoutSimplePagerItemBinding
import top.ntutn.zerohelper.vm.TestViewPagerActivityViewModel
import kotlin.random.Random


class TestViewPager2Activity : BaseActivity() {
    companion object {
        private const val TAG = "TestViewPager2Activity"

        fun actionStart(context: Context) {
            context.startActivity(Intent(context, TestViewPager2Activity::class.java))
        }
    }

    private val activityViewModel by viewModels<TestViewPagerActivityViewModel>()
    private lateinit var binding: ActivityTestViewPager2Binding
    private val mainHandler = Handler(Looper.getMainLooper())

    private val onPageChangedCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Log.i(TAG, "onPageSelected() invoked")
            activityViewModel.changeItemPosition(position)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            Log.i(TAG, "onPageScrolled() invoked")
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            Log.i(TAG, "onPageScrollStateChanged() invoked")
            super.onPageScrollStateChanged(state)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestViewPager2Binding.inflate(layoutInflater)
        setContentView(binding.root)
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。
//        binding.viewPager2.fitsSystemWindows = true


        binding.viewPager2.adapter = ViewPager2FragmentAdapter(supportFragmentManager, lifecycle)
        binding.viewPager2.registerOnPageChangeCallback(onPageChangedCallback)
        binding.viewPager2.offscreenPageLimit = 1

        activityViewModel.currentPageNumber.observe(this) {
            binding.viewPager2.currentItem = it
        }
//        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    mainHandler.post {
//                        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//                        PixelCopy.request(window, bitmap, {
//                            if (it == PixelCopy.SUCCESS) {
//                                val color = bitmap.getPixel(0, 0)
//                                window.statusBarColor = color
//                            }
//                        }, Handler(Looper.getMainLooper()))
//                    }
//                } else {
//                    val bitmap = window.decorView.drawingCache
//                    val color = bitmap.getPixel(0, 0)
//                    window.statusBarColor = color
//                }
//            }
//        })
        val drawRect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(drawRect)
        drawRect.top = drawRect.bottom - 1
        drawRect.left = drawRect.right / 2 - 1
        drawRect.right = drawRect.right / 2
//        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    mainHandler.post {
//                        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//                        PixelCopy.request(window, drawRect, bitmap, {
//                            if (it == PixelCopy.SUCCESS) {
//                                val color = bitmap.getPixel(0, 0)
//                                window.navigationBarColor = color
//                            }
//                        }, Handler(Looper.getMainLooper()))
//                    }
//                } else {
//                    val bitmap = window.decorView.drawingCache
//                    val color = bitmap.getPixel(0, 0)
//                    window.navigationBarColor = color
//                }
//            }
//        })
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager2.unregisterOnPageChangeCallback(onPageChangedCallback)
    }
}

class ViewPager2FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val random = Random(position)
        val colorInt = Color.rgb(random.nextInt(0, 256), random.nextInt(0, 256), random.nextInt(0, 256))
        return TestViewPager2Fragment.create(position, colorInt)
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

    }
}

class ViewPager2Adapter : RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>() {
    abstract class ViewHolder(viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        abstract fun bind(data: Pair<String, Int>)
    }

    class SimpleViewHolder(val viewBinding: LayoutSimplePagerItemBinding) : ViewHolder(viewBinding) {
        override fun bind(data: Pair<String, Int>) {
            viewBinding.textView.text = data.first
            viewBinding.root.setBackgroundColor(data.second)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SimpleViewHolder(LayoutSimplePagerItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val random = Random(position)
        val colorInt = Color.rgb(random.nextInt(0, 256), random.nextInt(0, 256), random.nextInt(0, 256))
        holder.bind("第${position}页" to colorInt)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}