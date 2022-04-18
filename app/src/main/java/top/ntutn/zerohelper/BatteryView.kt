package top.ntutn.zerohelper

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import top.ntutn.zerohelper.util.dp
import top.ntutn.zerohelper.util.dpFloat
import kotlin.math.atan
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * 电池组件
 * 设计稿 [https://www.figma.com/file/0R2ONNU57uwpBbGYNTfCZW/%E9%A6%96%E9%A1%B5---02-%E5%86%85%E5%AE%B9%E6%B6%88%E8%B4%B9?node-id=3230%3A26303]
 * @author liuhaixin.zero
 */
class BatteryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "BatteryView"
        private val SQRT_2 = sqrt(2f)
    }

    private var scale = 1f
    var value = 0.7f
        set(value) {
            if (value !in 0f..100f) {
                Log.wtf(TAG, "输入电量值参数错误！$value 不在[0, 100]范围")
                return
            }
            field = value
            invalidate()
        }
    var isCharging = false
        set(value) {
            field = value
            invalidate()
        }
    private val borderPaintProto by lazy {
        Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.STROKE
            alpha = (0.35 * 255f).toInt()
        }
    }
    private val borderPaint
        get() = borderPaintProto.apply {
            strokeWidth = 1f.dpFloat * scale
        }

    private val capPaint by lazy {
        Paint(borderPaint).apply {
            strokeWidth = 0f
            style = Paint.Style.FILL
        }
    }

    private val contentPaintProto by lazy {
        Paint(capPaint)
    }
    private val contentPaint
        get() = contentPaintProto.apply {
            color = if (isCharging) {
                Color.GREEN
            } else {
                Color.WHITE
            }
            alpha = 255
        }

    private val chargingBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ntutn_charging)
    }
    private val chargingMatrixProto by lazy {
        Matrix()
    }
    private val chargingMatrix
        get() = chargingMatrixProto.apply {
            val targetHeight = height - 2f.dpFloat * scale
            val currentHeight = chargingBitmap.height
            setTranslate((3.71f + 0.77f).dpFloat * scale, 1f.dpFloat * scale)
            preScale(targetHeight / currentHeight, targetHeight / currentHeight)
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = getMySize(widthMeasureSpec, 20.dp)
        val height = getMySize(heightMeasureSpec, 12.dp)
        // 保证等比例缩放
        if (width.toFloat() / 20f < height.toFloat() / 12) {
            scale = width.toFloat() / 20f.dpFloat
            setMeasuredDimension(width, (12.dp * scale).roundToInt())
        } else {
            scale = height.toFloat() / 12f.dpFloat
            setMeasuredDimension((20.dp * scale).roundToInt(), height)
        }
    }

    private fun getMySize(measureSpec: Int, defaultSize: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(specSize, defaultSize)
            else -> defaultSize
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas ?: return)

        val batteryHeight = height - 2f.dpFloat * scale // 高度减上下空白
        val batteryWidth = width - 3f.dpFloat * scale // 宽度减左右空白
        val batteryLeft = 0.77f.dpFloat * scale
        val batteryTop = 1f.dpFloat * scale

        // 绘制边框
        canvas.drawRoundRect(
            batteryLeft + borderPaint.strokeWidth / 2,
            batteryTop + borderPaint.strokeWidth / 2,
            batteryLeft + batteryWidth - borderPaint.strokeWidth / 2, // 实际绘制时要考虑画笔笔画宽度
            batteryTop + batteryHeight - borderPaint.strokeWidth / 2,
            2.5f.dpFloat * scale / SQRT_2,
            2.5f.dpFloat * scale / SQRT_2,
            borderPaint
        )

        // 绘制电池帽
        val capLeft = batteryLeft + batteryWidth + 0.5f.dpFloat * scale
        val capWith = 0.96f.dpFloat * scale
        val capHeight = 2.89f.dpFloat * scale
        val capTop = height / 2 - capHeight / 2
        val angle = atan(capHeight / 2 / 0.5f.dpFloat / scale) * 180 / kotlin.math.PI.toFloat()

        canvas.drawArc(
            batteryLeft + batteryWidth - 0.5f.dpFloat * scale - capWith,
            capTop,
            capLeft + capWith,
            capTop + capHeight,
            -angle,
            angle * 2,
            false,
            capPaint
        )

        // 绘制中间电量
        val contentTop = 2.5f.dpFloat * scale
        val contentLeft = 2.27f.dpFloat * scale
        val contentMaxWith = 17f.dpFloat * scale - 3f.dpFloat * scale
        val contentHeight = batteryHeight - 3f.dpFloat * scale
        canvas.drawRoundRect(
            contentLeft,
            contentTop,
            contentLeft + contentMaxWith * value,
            contentTop + contentHeight,
            1f.dpFloat * scale / SQRT_2,
            1f.dpFloat * scale / SQRT_2,
            contentPaint
        )

        // 绘制小闪电标志
        if (isCharging) {
            canvas.drawBitmap(chargingBitmap, chargingMatrix, null)
        }
    }
}