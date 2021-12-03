package top.ntutn.zerohelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import top.ntutn.zerohelper.databinding.AboutMenuTitleBinding
import top.ntutn.zerohelper.databinding.AboutMenuUserBinding
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
            title(getString(R.string.app_name), "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})")
            user(
                avatar = "file:///android_asset/img/ntutn_avatar.jpg",
                name = "归零幻想",
                description = """
                    email: ntutn.top@gmail.com
                    blog: https://ntutn.top
                """.trimIndent(),
            )
//            repeat(3) {
//                link()
//            }
//            repeat(5) {
//                license()
//            }
        })
    }
}

class AboutAdapter(private val menuList: List<MenuItem>) : RecyclerView.Adapter<AboutAdapter.AboutMenuViewHolder>() {
    abstract class AboutMenuViewHolder(viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        abstract fun bind(item: MenuItem)
    }

    class TitleMenuViewHolder(val viewBinding: AboutMenuTitleBinding) : AboutMenuViewHolder(viewBinding) {
        override fun bind(item: MenuItem) {
            require(item is MenuTitleItem)
            viewBinding.descriptionTextView.text = "${item.appName}\nversion:${item.appVersion}"
        }
    }

    class UserMenuViewHolder(val viewBinding: AboutMenuUserBinding) : AboutMenuViewHolder(viewBinding) {
        override fun bind(item: MenuItem) {
            require(item is MenuUserItem)
            Glide.with(viewBinding.root)
                .load(item.avatar)
                .circleCrop()
                .into(viewBinding.menuAvatar)
            viewBinding.menuName.text = item.name
            viewBinding.menuDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutMenuViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MenuItem.ItemType.TITLE.ordinal -> TitleMenuViewHolder(
                AboutMenuTitleBinding.inflate(layoutInflater, parent, false)
            )
            MenuItem.ItemType.USER.ordinal -> UserMenuViewHolder(
                AboutMenuUserBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw NotImplementedError("有未实现的类型")
        }
    }

    override fun onBindViewHolder(holder: AboutMenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int = menuList.size

    override fun getItemViewType(position: Int): Int {
        return menuList[position].itemType.ordinal
    }
}

abstract class MenuItem {
    abstract val itemType: MenuItem.ItemType

    enum class ItemType {
        TITLE,
        LICENSE,
        LINK,
        USER,
    }
}

class MenuTitleItem(val appName: String, val appVersion: String) : MenuItem() {
    override val itemType: ItemType = ItemType.TITLE
}

class MenuLicenseItem : MenuItem() {
    override val itemType: ItemType = ItemType.LICENSE
}

class MenuLinkItem : MenuItem() {
    override val itemType: ItemType = ItemType.LINK
}

class MenuUserItem(val avatar:String, val name: String, val description: String) : MenuItem() {
    override val itemType: ItemType = ItemType.USER
}

fun MutableList<MenuItem>.title(appName: String, appVersion: String) {
    add(MenuTitleItem(appName, appVersion))
}

fun MutableList<MenuItem>.license() {
    add(MenuLicenseItem())
}

fun MutableList<MenuItem>.link() {
    add(MenuLinkItem())
}

fun MutableList<MenuItem>.user(avatar:String, name: String, description: String) {
    add(MenuUserItem(avatar, name, description))
}