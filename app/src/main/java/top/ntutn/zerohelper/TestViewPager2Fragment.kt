package top.ntutn.zerohelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import top.ntutn.zerohelper.databinding.LayoutSimplePagerItemBinding

class TestViewPager2Fragment : Fragment() {
    companion object {
        private const val KEY_POSITION = "key_position"
        private const val KEY_COLOR = "key_color"
        private const val TAG = "TestViewPager2Fragment"

        fun create(position: Int, @ColorInt color: Int): TestViewPager2Fragment {
            return TestViewPager2Fragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_POSITION, position)
                    putInt(KEY_COLOR, color)
                }
            }
        }
    }

    private var _binding: LayoutSimplePagerItemBinding? = null
    private val binding
        get() = _binding!!
    private val activityViewModel by activityViewModels<TestViewPagerActivityViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(javaClass.simpleName, "$tag onAttach() invoked")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(javaClass.simpleName, "$tag onCreate() invoked")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(javaClass.simpleName, "$tag onCreateView() invoked")
        _binding = LayoutSimplePagerItemBinding.inflate(inflater, container, false)

        val color = arguments?.getInt(KEY_COLOR)
        val position = (arguments?.get(KEY_POSITION) as? Int) ?: 0
        binding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> return@setOnTouchListener true
                MotionEvent.ACTION_UP -> {
                    when (event.x) {
                        in 0.0..(v.width / 3.0) -> {
                            activityViewModel.previousPage()
                            return@setOnTouchListener true
                        }
                        in (v.width / 3.0 * 2)..v.width.toDouble() -> {
                            activityViewModel.nextPage()
                            return@setOnTouchListener true
                        }
                        else -> {
                            Log.i(TAG, "弹窗")
                            BottomSheetDialog(context ?: return@setOnTouchListener true)
                                .apply {
                                    val view = TextView(binding.root.context)
                                    view.text = "这是个弹窗"
                                    view.setBackgroundColor(Color.GREEN)
                                    view.layoutParams =
                                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)

                                    val layout = LinearLayout(binding.root.context)
                                    layout.orientation = LinearLayout.VERTICAL
                                    layout.addView(view)
                                    layout.layoutParams =
                                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400)
                                    layout.setBackgroundColor(listOf(Color.WHITE, Color.BLACK)[position % 2])

                                    setContentView(layout)
//                                    window?.decorView?.postDelayed({
//                                        val drawRect = Rect()
//                                        window?.decorView?.getWindowVisibleDisplayFrame(drawRect)?:return@postDelayed
//                                        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            PixelCopy.request(window!!, drawRect, bitmap, {
//                                                if (it == PixelCopy.SUCCESS) {
//                                                    val color = bitmap.getPixel(0, 0)
////                                                    window!!.navigationBarColor = color
//                                                }
//                                            }, Handler(Looper.getMainLooper()))
//                                        }
//                                    },500L)
                                }
                                .show()
//                            activityViewModel.changeItemPosition((activityViewModel.currentPageNumber.value ?: 0) + 10)
                            return@setOnTouchListener true
                        }
                    }
                }
            }
            false
        }


        if (color != null && position != null) {
            bindData("第${position}页" to color)
        }

        return binding.root
    }

    fun bindData(data: Pair<String, Int>) {
        binding.textView.text = data.first
        binding.root.setBackgroundColor(data.second)
    }

    override fun onStart() {
        super.onStart()
        Log.d(javaClass.simpleName, "$tag onStart() invoked")
    }

    override fun onResume() {
        super.onResume()
        Log.d(javaClass.simpleName, "$tag onResume() invoked")
    }

    override fun onPause() {
        super.onPause()
        Log.d(javaClass.simpleName, "$tag onPause() invoked")
    }

    override fun onStop() {
        super.onStop()
        Log.d(javaClass.simpleName, "$tag onStop() invoked")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(javaClass.simpleName, "$tag onDestroyView() invoked")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "$tag onDestroy() invoked")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(javaClass.simpleName, "$tag onDetach() invoked")
    }
}