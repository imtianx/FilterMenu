package cn.imtianx.filtermenu

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val headers = arrayOf("全部区域", "距离优先")
    private val areaData = arrayOf("全部区域", "余杭区", "拱墅区", "西湖区", "滨江区")
    private val sortData = arrayOf("距离排序", "好评排序")
    private val menuView = ArrayList<View>()


    private var areaAdapter: FilterMenuListAdapter? = null
    private var sortAdapter: FilterMenuListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFilterMenu()


    }

    private fun initFilterMenu() {
        val girdArea = GridView(this)
        girdArea.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        girdArea.setBackgroundColor(Color.WHITE)
        girdArea.numColumns = 4
        girdArea.horizontalSpacing = 4
        girdArea.verticalSpacing = 4

        val areaDtas = ArrayList<MenuItemData>()
        areaData.forEach {
            areaDtas.add(MenuItemData("",it))
        }
        areaAdapter = FilterMenuListAdapter(data = areaDtas)
        girdArea.adapter = areaAdapter

        val listSort = ListView(this)
        listSort.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        listSort.setBackgroundColor(Color.WHITE)

        val sortDtas = ArrayList<MenuItemData>()
        sortData.forEach {
            sortDtas.add(MenuItemData("",it))
        }
        sortAdapter = FilterMenuListAdapter(data = sortDtas)
        listSort.dividerHeight = 0
        listSort.adapter = sortAdapter


        menuView.add(girdArea)
        menuView.add(listSort)

        girdArea.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            areaAdapter!!.setCheckItem(position)
            filterMenu.setTabText(if (position == 0) headers[0] else areaData[position])
            filterMenu.closeMenu()
        }

        listSort.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            sortAdapter!!.setCheckItem(position)
            filterMenu.setTabText(if (position == 0) headers[1] else sortData[position])
            filterMenu.closeMenu()
        }


        filterMenu.setFilterMenu(Arrays.asList(*headers), menuView)


    }
}
