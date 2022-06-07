package top.ntutn.zerohelper.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.*
import androidx.fragment.app.Fragment


/**
 * Created by admin on 2020/5/28.
 */
internal data class NavigationBarAttr(val color: Int, val isLight: Boolean)

internal object NavigationBarUtil {

    @JvmStatic
    fun Window.navigationBarLight() = setNavigationBarStyleInner(this, Color.WHITE, true)

    @JvmStatic
    fun Activity.navigationBarLight() = window?.navigationBarLight()

    @JvmStatic
    fun Fragment.navigationBarLight() = activity?.navigationBarLight()

    @JvmStatic
    fun Window.navigationBarDark() = setNavigationBarStyleInner(this, Color.BLACK, false)

    @JvmStatic
    fun Activity.navigationBarDark() = window?.navigationBarDark()

    @JvmStatic
    fun Fragment.navigationBarDark() = activity?.navigationBarDark()

    @JvmStatic
    fun Window.navigationBarAutoColor() {
        navigationBarLight()
    }

    @JvmStatic
    fun Activity.navigationBarAutoColor() {
        navigationBarLight()
    }

    @JvmStatic
    fun Fragment.navigationBarAutoColor() {
        navigationBarLight()
    }

    @JvmStatic
    fun Activity.getCurrentNavigationBarAttr(): NavigationBarAttr? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val flag = window.decorView.systemUiVisibility
            val isLight = (flag and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0
            NavigationBarAttr(window.navigationBarColor, isLight)
        } else {
            null
        }
    }

    @JvmStatic
    fun Fragment.getCurrentNavigationBarAttr(): NavigationBarAttr? = activity?.getCurrentNavigationBarAttr()

    @JvmStatic
    fun Activity.recoverNavigationBarAttr(attr: NavigationBarAttr?) {
        attr ?: return
        setNavigationBarStyleInner(window, attr.color, attr.isLight)
    }

    @JvmStatic
    fun Fragment.recoverNavigationBarAttr(attr: NavigationBarAttr?) = activity?.recoverNavigationBarAttr(attr)


    @JvmStatic
    fun setLightNavigationBarColor(window: Window, isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val flag = window.decorView.systemUiVisibility
            val newFlag = if (isLight) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                flag or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                flag and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            if (newFlag != flag) {
                window.decorView.systemUiVisibility = newFlag
            }
        }
    }

    private fun setNavigationBarStyleInner(window: Window, navigationBarColor: Int, isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = navigationBarColor
            setLightNavigationBarColor(window, isLight)
        }
    }

    @JvmStatic
    fun hasPhysicalKeys(ctx: Context): Boolean {
        val hasMenuKey: Boolean = ViewConfiguration.get(ctx).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
        // OPPO CPH1851 has no physical keys but 'hasBackKey' returns true while 'hasHomeKey' returns false.
        // Combine KEYCODE_BACK and KEYCODE_HOME together
        return  hasMenuKey || (hasBackKey && hasHomeKey)
    }

    private fun getViewBottomLocation(view: View?): Int {
        view ?: return 0
        val pos = IntArray(2)
        view.getLocationOnScreen(pos)
        return view.height + pos[1]
    }

    /**
     * detect whether a virtual navigation bar exists
     * this method is valid after Activity#onWindowFocusChanged(boolean hasFocus) is called
     */
    @JvmStatic
    fun hasVirtualNavigationBar(activity: Activity): Boolean {
        val decor = activity.window.decorView
        val realBottom = getViewBottomLocation(decor)
        val contentBottom = getViewBottomLocation(decor.findViewById(android.R.id.content))
        return realBottom != contentBottom
    }
}