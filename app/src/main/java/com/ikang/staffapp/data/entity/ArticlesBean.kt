package com.ikang.staffapp.data.entity


data class HomeListBean(
    val curPage: Int,
    val datas: List<ArticlesBean>
)

data class ArticlesBean(
    var title: String? = null,
    var desc: String? = null
)
