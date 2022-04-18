package top.ntutn.zerohelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import top.ntutn.zerohelper.databinding.ActivityTestBatteryViewBinding
import top.ntutn.zerohelper.util.dp

class TestBatteryViewActivity: BaseActivity() {
    companion object {
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, TestBatteryViewActivity::class.java))
        }
    }

    private lateinit var binding: ActivityTestBatteryViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBatteryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.batteryView.value = 0f
        binding.previewSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.batteryView.layoutParams.width = progress.dp
                binding.batteryView.layoutParams.height = progress.dp
                binding.batteryView.requestLayout()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
        binding.batteryValue.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.batteryView.value = progress.toFloat() / 100f
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
        binding.chargingCheckbox.setOnCheckedChangeListener { _, isChecked ->
            binding.batteryView.isCharging = isChecked
        }
    }
}