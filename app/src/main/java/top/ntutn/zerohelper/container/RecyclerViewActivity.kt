package top.ntutn.zerohelper.container

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.zerohelper.R
import top.ntutn.zerohelper.databinding.ActivityRecyclerViewBinding
import top.ntutn.zerohelper.databinding.ItemCommentLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewActivity : AppCompatActivity() {
    companion object {
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, RecyclerViewActivity::class.java))
        }
    }

    private lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindView()
    }

    private fun bindView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val dataList = mutableListOf<CommentModel>().apply {
            repeat(80) {
                add(
                    CommentModel(
                        commentId = UUID.randomUUID().toString(),
                        time = (0L..360_0000).random(),
                        text = "这是第${it}条评论",
                        liked = listOf(true, false).random()
                    )
                )
            }
        }.sortedBy { it.time }
        binding.recyclerView.adapter = PayloadAdapter(dataList) {
            val adapter = binding.recyclerView.adapter as PayloadAdapter
            var position = -1
            for (i in dataList.indices) {
                val data = dataList[i]
                if (data.commentId == it.commentId) {
                    position = i
                }
            }
            if (position >= 0) {
                dataList[position].liked = !dataList[position].liked
                adapter.notifyItemChanged(position, 1)
            }
        }
    }
}

data class CommentModel(
    val commentId: String,
    val time: Long,
    val text: String,
    var liked: Boolean
)

class PayloadAdapter(private val dataList: List<CommentModel>, private val likeCallback: (model: CommentModel) -> Unit) :
    RecyclerView.Adapter<PayloadAdapter.ViewHolder>() {
    companion object {
        private val SIMPLE_DATE_FORMAT = SimpleDateFormat("mm:ss")
    }

    class ViewHolder(val binding: ItemCommentLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCommentLayoutBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.binding.root.context
        val data = dataList[position]
        holder.binding.timeTextView.text = SIMPLE_DATE_FORMAT.format(data.time)
        holder.binding.contentTextView.text = data.text
        val colorStateList = if (data.liked) {
            AppCompatResources.getColorStateList(context, R.color.red)
        } else {
            AppCompatResources.getColorStateList(context, R.color.black)
        }
        ImageViewCompat.setImageTintList(holder.binding.likeButton, colorStateList)
        holder.binding.likeButton.setOnClickListener { likeCallback(data) }
    }

    override fun getItemCount(): Int = dataList.size
}