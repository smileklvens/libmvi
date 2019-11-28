package com.ikang.staffapp.widget

import android.content.Context
import android.util.AttributeSet
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ikang.staffapp.R


/*
    底部导航
 */
class BottomNavBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationBar(context, attrs, defStyleAttr) {

    //购物车Tab 标签
//    private val mCartBadge: TextBadgeItem
//    //消息Tab 标签
//    private val mMsgBadge: ShapeBadgeItem

    init {
        //首页
        val homeItem = BottomNavigationItem(
            R.drawable.btn_nav_home_press,
            resources.getString(R.string.nav_bar_home)
        )
            .setInactiveIconResource(R.drawable.btn_nav_home_normal)
            .setActiveColorResource(R.color.common_blue)
            .setInActiveColorResource(R.color.text_normal)
        //我的
        val userItem = BottomNavigationItem(
            R.drawable.btn_nav_user_press,
            resources.getString(R.string.nav_bar_user)
        )
            .setInactiveIconResource(R.drawable.btn_nav_user_normal)
            .setActiveColorResource(R.color.common_blue)
            .setInActiveColorResource(R.color.text_normal)

        //设置底部导航模式及样式
        setMode(MODE_FIXED)
        setBackgroundStyle(BACKGROUND_STYLE_STATIC)
        setBarBackgroundColor(R.color.common_white)
        //添加Tab
        addItem(homeItem)
            .addItem(userItem)
            .setFirstSelectedPosition(0)
            .initialise()
    }


}
