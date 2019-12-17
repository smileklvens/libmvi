package com.ikang.mymodule.entity


data class HomeListBean(
    val curPage: Int,
    val datas: List<ArticlesBean>
)

data class ArticlesBean(
    var title: String,
    var desc: String,
    var url: String
)
