package top.ntutn.zerohelper.container

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import top.ntutn.zerohelper.BuildConfig
import top.ntutn.zerohelper.R
import top.ntutn.zerohelper.base.BaseActivity
import top.ntutn.zerohelper.databinding.AboutMenuLicenseBinding
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

//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);       //设置沉浸式状态栏，在MIUI系统中，状态栏背景透明。原生系统中，状态栏背景半透明。
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。
        binding.aboutRecyclerView.fitsSystemWindows = true

        title = "关于"

        binding.aboutRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.aboutRecyclerView.adapter = AboutAdapter(mutableListOf<MenuItem>().apply {
            title(
                getString(R.string.app_name),
                "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
            )
            user(
                avatar = "file:///android_asset/img/ntutn_avatar.jpg",
                name = "归零幻想",
                description = """
                    email: ntutn.top@gmail.com
                    blog: https://ntutn.top
                """.trimIndent(),
                "https://ntutn.top",
            )
            license(
                componentName = "Glide",
                companyName = "Google",
                MenuLicenseItem.License.GLIDE,
                "https://github.com/bumptech/glide"
            )
            license(
                componentName = "LiveData",
                companyName = "Google",
                MenuLicenseItem.License.MIT,
                "https://developer.android.com/topic/libraries/architecture/livedata"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
            license(
                componentName = "Retrtofit",
                companyName = "Squareup",
                MenuLicenseItem.License.APACHE_2,
                "https://square.github.io/retrofit/"
            )
        })
    }
}

class AboutAdapter(private val menuList: List<MenuItem>) :
    RecyclerView.Adapter<AboutAdapter.AboutMenuViewHolder>() {
    abstract class AboutMenuViewHolder(viewBinding: ViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        abstract fun bind(item: MenuItem)
    }

    class TitleMenuViewHolder(val viewBinding: AboutMenuTitleBinding) :
        AboutMenuViewHolder(viewBinding) {
        override fun bind(item: MenuItem) {
            require(item is MenuTitleItem)
            viewBinding.descriptionTextView.text = "${item.appName}\nversion:${item.appVersion}"
        }
    }

    class UserMenuViewHolder(val viewBinding: AboutMenuUserBinding) :
        AboutMenuViewHolder(viewBinding) {
        override fun bind(item: MenuItem) {
            require(item is MenuUserItem)
            Glide.with(viewBinding.root)
                .load(item.avatar)
                .circleCrop()
                .into(viewBinding.menuAvatar)
            viewBinding.menuName.text = item.name
            viewBinding.menuDescription.text = item.description
            if (!item.link.isNullOrBlank()) {
                viewBinding.root.setOnClickListener {
                    val uri = Uri.parse(item.link)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    it.context.startActivity(intent)
                }
            } else {
                viewBinding.root.isClickable = false
            }
        }
    }

    class LicenseMenuViewHolder(val viewBinding: AboutMenuLicenseBinding) :
        AboutMenuViewHolder(viewBinding) {
        override fun bind(item: MenuItem) {
            require(item is MenuLicenseItem)
            viewBinding.menuName.text = item.componentName
            viewBinding.menuCompany.text = item.companyName
            viewBinding.menuLicense.text = Html.fromHtml(
                """
                <a href="${item.license.link()}">${item.license.showName()}</a>
            """.trimIndent()
            )
            viewBinding.menuLicense.setOnClickListener {
                val uri = Uri.parse(item.license.link())
                val intent = Intent(Intent.ACTION_VIEW, uri)
                it.context.startActivity(intent)
            }
            viewBinding.menuHomePage.text = item.homepage
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
            MenuItem.ItemType.LICENSE.ordinal -> LicenseMenuViewHolder(
                AboutMenuLicenseBinding.inflate(layoutInflater, parent, false)
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
    abstract val itemType: ItemType

    enum class ItemType {
        TITLE,
        LICENSE,
        USER,
    }
}

class MenuTitleItem(val appName: String, val appVersion: String) : MenuItem() {
    override val itemType: ItemType = ItemType.TITLE
}

class MenuLicenseItem(
    val componentName: String,
    val companyName: String,
    val license: License,
    val homepage: String
) :
    MenuItem() {
    override val itemType: ItemType = ItemType.LICENSE

    enum class License {
        MIT,
        APACHE_2,
        GLIDE;

        fun link() = when (this) {
            MIT -> "https://opensource.org/licenses/MIT"
            APACHE_2 -> "https://www.apache.org/licenses/LICENSE-2.0"
            GLIDE -> "https://github.com/bumptech/glide/raw/master/LICENSE"
        }

        fun showName() = when (this) {
            MIT -> "MIT License"
            APACHE_2 -> "Apache License Version 2.0"
            GLIDE -> "View License"
        }
    }
}

class MenuUserItem(
    val avatar: String,
    val name: String,
    val description: String,
    val link: String?
) : MenuItem() {
    override val itemType: ItemType = ItemType.USER
}

fun MutableList<MenuItem>.title(appName: String, appVersion: String) {
    add(MenuTitleItem(appName, appVersion))
}

fun MutableList<MenuItem>.license(
    componentName: String,
    companyName: String,
    license: MenuLicenseItem.License,
    homepage: String
) {
    add(MenuLicenseItem(componentName, companyName, license, homepage))
}

fun MutableList<MenuItem>.user(avatar: String, name: String, description: String, link: String?) {
    add(MenuUserItem(avatar, name, description, link))
}