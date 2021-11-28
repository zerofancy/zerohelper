package top.ntutn.zerohelper

import android.os.Bundle
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

        viewModel.result.observe(this) {
            AlertDialog.Builder(this)
                .setTitle("Check for Update")
                .setMessage(it)
                .create()
                .show()
        }

        binding.test.setOnClickListener {
            viewModel.checkForUpdate()
        }
    }
}