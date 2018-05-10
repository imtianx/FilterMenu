package cn.imtianx.filtermenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * <pre>
 *     @desc: menu adapter
 *</pre>
 *
 * @author 奚岩
 * @date 2018/5/5 下午2:50
 */
class FilterMenuListAdapter(private val layoutId: Int = R.layout.item_menu_list, private val data: List<MenuItemData>) : BaseAdapter() {
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(layoutId, null)
            viewHolder = ViewHolder(view!!)
            view.tag = viewHolder

        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.mtvName.text = data[position].text

        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mtvName.setTextColor(parent.context.resources.getColor(R.color.drop_down_selected))
            } else {
                viewHolder.mtvName.setTextColor(parent.context.resources.getColor(R.color.drop_down_unselected))
            }
        }

        return view
    }

    internal class ViewHolder(itemView: View) {
        var mtvName: TextView = itemView.findViewById(R.id.tv_menu_item_name) as TextView

    }
}
