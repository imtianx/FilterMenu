package cn.imtianx.filtermenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * <pre>
 * @desc:
</pre> *
 *
 * @author 奚岩
 * @date 2018/5/5 下午12:50
 */
class FilterMenuListAdapter(private val layoutId: Int, private val data: List<String>) : BaseAdapter() {
    private var checkItemPosition = 0

    fun setCheckItem(position: Int) {
        checkItemPosition = position
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(i: Int): Any {
        return data[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(viewGroup.context).inflate(layoutId, null)
            viewHolder = ViewHolder(view!!)
            view.tag = viewHolder

        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.mtvName.text = data[i]

        if (checkItemPosition != -1) {
            if (checkItemPosition == i) {
                viewHolder.mtvName.setTextColor(viewGroup.context.resources.getColor(R.color.drop_down_selected))
            } else {
                viewHolder.mtvName.setTextColor(viewGroup.context.resources.getColor(R.color.drop_down_unselected))
            }
        }

        return view
    }

    internal class ViewHolder(itemView: View) {
        var mtvName: TextView = itemView.findViewById<View>(R.id.tv_menu_item_name) as TextView

    }
}
