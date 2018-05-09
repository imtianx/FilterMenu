package cn.imtianx.filtermenu

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * <pre>
 *     @desc: 下拉筛选控件
 * </pre>
 * @author imtianx
 * @date 2018/5/3 下午4:23
 */
class FilterMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private val tabMenuView: LinearLayout

    private val containerView: FrameLayout

    private var popupMenuViews: FrameLayout? = null

    private var maskView: View? = null

    private var curTabPosition = -1

    private var dividerColor = -0x333334
    private var tabSelectColor = -0x76f37b
    private var tabUnSelectColor = -0xeeeeef
    private var maskColor = -0x7f000001
    private var menuTextSize = 14

    private var menuSelectedIcon: Int
    private var menuUnSelectedIcon: Int

    private var tabHeight = 44

    val isShow: Boolean
        get() = (curTabPosition != -1)

    init {

        orientation = LinearLayout.VERTICAL

        var menuBgColor = -0x1
        var underlineColor = dividerColor

        // add custom attrs
        val a = context.obtainStyledAttributes(attrs, R.styleable.FilterMenuView)
        underlineColor = a.getColor(R.styleable.FilterMenuView_underlineColor, underlineColor)
        dividerColor = a.getColor(R.styleable.FilterMenuView_dividerColor, dividerColor)
        tabSelectColor = a.getColor(R.styleable.FilterMenuView_tabSelectedColor, tabSelectColor)
        tabUnSelectColor = a.getColor(R.styleable.FilterMenuView_tabUnselectedColor, tabUnSelectColor)
        menuBgColor = a.getColor(R.styleable.FilterMenuView_menuBgColor, menuBgColor)
        maskColor = a.getColor(R.styleable.FilterMenuView_maskColor, maskColor)
        menuTextSize = a.getDimensionPixelSize(R.styleable.FilterMenuView_menuTextSize, menuTextSize)
        menuSelectedIcon = a.getResourceId(R.styleable.FilterMenuView_menuSelectedIcon, R.mipmap.fm_selected_icon)
        menuUnSelectedIcon = a.getResourceId(R.styleable.FilterMenuView_menuUnSelectedIcon, R.mipmap.fm_unselected_icon)
        tabHeight = a.getDimensionPixelSize(R.styleable.FilterMenuView_tabHeight, dp2px(tabHeight.toFloat()))
        a.recycle()

        // add top line
        val topLine = View(getContext())
        topLine.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(1f))
        topLine.setBackgroundColor(underlineColor)
        addView(topLine, 0)

        //add tab menu
        tabMenuView = LinearLayout(context)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        tabMenuView.orientation = LinearLayout.HORIZONTAL
        tabMenuView.gravity = Gravity.CENTER
        tabMenuView.setBackgroundColor(menuBgColor)
        tabMenuView.layoutParams = params
        addView(tabMenuView, 1)

        // add bottom line
        val bottomLine = View(getContext())
        bottomLine.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(1f))
        bottomLine.setBackgroundColor(underlineColor)
        addView(bottomLine, 2)

        // init menu container
        containerView = FrameLayout(context)
        containerView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)

    }

    /**
     *  set header and views
     */
    fun setFilterMenu(tabHeaders: List<String>, popViews: List<View>) {

        if (tabHeaders.size != popViews.size) {
            throw  IllegalArgumentException("params not match, tabHeaders.size() should be equal popViews.size()")
        }

        for (i in tabHeaders.indices) {
            addTab(tabHeaders, i)
        }

        maskView = View(context)
        maskView?.let {
            it.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT)
            it.setBackgroundColor(maskColor)
            it.setOnClickListener { closeMenu() }
            containerView.addView(it, 0)
            it.visibility = View.GONE
        }

        if (containerView.getChildAt(1) != null) {
            containerView.removeViewAt(1)
        }

        popupMenuViews = FrameLayout(context)
        popupMenuViews?.let {
            it.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            it.visibility = View.GONE
            containerView.addView(it, 1)
        }

        for (i in popViews.indices) {
            popViews[i].layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            popupMenuViews!!.addView(popViews[i], i)
        }
    }

    private fun addTab(tabHeaders: List<String>, index: Int) {
        val tabView = View.inflate(context, R.layout.layout_filter_menu_tab, null)
        tabView.layoutParams = LinearLayout.LayoutParams(0, tabHeight, 1.0f)
        val tab = getTabTextView(tabView)
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize.toFloat())
        tab.setTextColor(tabUnSelectColor)
        tab.text = tabHeaders[index]
        getTabIcon(tabView).setImageDrawable(resources.getDrawable(menuUnSelectedIcon))
        // set listener
        tabView.setOnClickListener { switchMenu(tabView) }
        tabMenuView.addView(tabView)

        // add line
        if (index < tabHeaders.size - 1) {
            val view = View(context)
            view.layoutParams = LinearLayout.LayoutParams(dp2px(0.5f),
                    ViewGroup.LayoutParams.MATCH_PARENT)
            view.setBackgroundColor(dividerColor)
            tabMenuView.addView(view)
        }
    }

    private fun getTabTextView(tabView: View): TextView {
        return tabView.findViewById(R.id.tv_tab_name) as TextView
    }

    private fun setTabTextColor(index: Int, colorId: Int) {
        getTabTextView(tabMenuView.getChildAt(index)).setTextColor(colorId)
    }

    private fun getTabIcon(tabView: View): ImageView {
        return tabView.findViewById(R.id.iv_tab_icon) as ImageView
    }

    private fun setTabIcon(index: Int, iconId: Int) {
        getTabIcon(tabMenuView.getChildAt(index))
                .setImageDrawable(resources.getDrawable(iconId))
    }

    fun setTabText(text: String) {
        if (curTabPosition != -1) {
            getTabTextView(tabMenuView.getChildAt(curTabPosition)).text = text
        }
    }

    fun closeMenu() {
        if (this.getChildAt(3) != null) {
            removeView(containerView)
        }
        if (curTabPosition != -1) {
            setTabTextColor(curTabPosition, tabUnSelectColor)
            setTabIcon(curTabPosition, menuUnSelectedIcon)
            popupMenuViews!!.visibility = View.GONE
            popupMenuViews!!.animation = AnimationUtils.loadAnimation(context, R.anim.fm_menu_out)
            maskView!!.visibility = View.GONE
            maskView!!.animation = AnimationUtils.loadAnimation(context, R.anim.fm_mask_out)
            curTabPosition = -1
        }
    }

    private fun switchMenu(target: View) {
        if (this.getChildAt(3) == null) {
            addView(containerView, 3)
        }
        println(curTabPosition)
        var i = 0
        while (i < tabMenuView.childCount) {
            if (target === tabMenuView.getChildAt(i)) {
                if (curTabPosition == i) {
                    closeMenu()
                } else {
                    if (curTabPosition == -1) {
                        popupMenuViews!!.visibility = View.VISIBLE
                        popupMenuViews!!.animation = AnimationUtils.loadAnimation(context, R.anim.fm_menu_in)
                        maskView!!.visibility = View.VISIBLE
                        maskView!!.animation = AnimationUtils.loadAnimation(context, R.anim.fm_mask_in)
                        popupMenuViews!!.getChildAt(i / 2).visibility = View.VISIBLE
                    } else {
                        popupMenuViews!!.getChildAt(i / 2).visibility = View.VISIBLE
                    }
                    curTabPosition = i
                    setTabTextColor(i, tabSelectColor)
                    setTabIcon(i, menuSelectedIcon)
                }
            } else {
                setTabTextColor(i, tabUnSelectColor)
                setTabIcon(i, menuUnSelectedIcon)
                popupMenuViews!!.getChildAt(i / 2).visibility = View.GONE
            }
            i += 2
        }
    }


    private fun dp2px(value: Float): Int {
        val dm = resources.displayMetrics
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5).toInt()
    }


}