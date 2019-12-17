package com.ikang.staffapp.data.entity

/**
 * @author ikang-renwei
 * @Date 2019/12/04 16:28
 * @describe 我的控件列表 实体
 */
data class MeListBean(
        val imgUrl: String,
        val name: String,
        val curPage: Int,
        val datas: List<MeListItemBean>
)

data class MeListItemBean(
        val imgUrl: String,
        val title: String,
        val content: String,
        val isShow: Boolean,
        val type: String//点击类型
)
