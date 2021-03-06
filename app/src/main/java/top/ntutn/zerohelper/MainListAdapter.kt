package top.ntutn.zerohelper

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.zerohelper.util.color
import top.ntutn.zerohelper.util.spFloat

class MainListAdapter(private val mainList: List<Item>) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>() {
    abstract class Item(open val text: String) {
        enum class Type {
            TITLE,
            ITEM
        }

        abstract val type: Type
    }

    class TitleItem(override val text: String) : Item(text) {
        override val type: Type = Type.TITLE
    }

    class ContentItem(override val text: String, val clickListener: (View) -> Unit) : Item(text) {
        override val type: Type = Type.ITEM
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class TitleViewHolder(view: View) : ViewHolder(view)

    class ItemViewHolder(view: View) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context)
        view.layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return if (viewType == Item.Type.TITLE.ordinal) {
            view.textSize = 10f.spFloat
            view.setBackgroundColor("#66ff0000".color)
            TitleViewHolder(view)
        } else {
            view.textSize = 8f.spFloat
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.itemView as TextView
        val data = mainList[position]
        textView.text = data.text
        if (data is ContentItem) {
            textView.setOnClickListener(data.clickListener)
        }
    }

    override fun getItemCount(): Int = mainList.size

    override fun getItemViewType(position: Int): Int {
        return mainList[position].type.ordinal
    }
}

internal fun MutableList<MainListAdapter.Item>.title(text: String) {
    add(MainListAdapter.TitleItem(text))
}

internal fun MutableList<MainListAdapter.Item>.item(text: String, lambda: (View) -> Unit) {
    add(MainListAdapter.ContentItem(text, lambda))
}