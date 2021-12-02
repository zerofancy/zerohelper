package top.ntutn.zerohelper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.zerohelper.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity() {
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "关于"

        binding.aboutRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.aboutRecyclerView.adapter = AboutAdapter(mutableListOf<MenuItem>().apply {
            user()
            repeat(3) {
                link()
            }
            repeat(5) {
                license()
            }
        })
    }
}

class AboutAdapter(private val menuList: List<MenuItem>) : RecyclerView.Adapter<AboutAdapter.AboutMenuViewHolder>() {
    open class AboutMenuViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutMenuViewHolder {
        return AboutMenuViewHolder(TextView(parent.context).apply { text = "sdfrgdthsg" })
    }

    override fun onBindViewHolder(holder: AboutMenuViewHolder, position: Int) {
        when (holder) {

        }
//        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = menuList.size

    override fun getItemViewType(position: Int): Int {
        return menuList[position].itemType.ordinal
    }
}

abstract class MenuItem {
    abstract val itemType: MenuItem.ItemType

    enum class ItemType {
        LICENSE,
        LINK,
        USER,
    }
}

class MenuLicenseItem : MenuItem() {
    override val itemType: ItemType = ItemType.LICENSE
}

class MenuLinkItem : MenuItem() {
    override val itemType: ItemType = ItemType.LINK
}

class MenuUserItem : MenuItem() {
    override val itemType: ItemType = ItemType.USER
}

fun MutableList<MenuItem>.license() {
    add(MenuLicenseItem())
}

fun MutableList<MenuItem>.link() {
    add(MenuLinkItem())
}

fun MutableList<MenuItem>.user() {
    add(MenuUserItem())
}