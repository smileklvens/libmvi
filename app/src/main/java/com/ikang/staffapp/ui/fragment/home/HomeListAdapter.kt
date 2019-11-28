package com.ikang.staffapp.ui.fragment.home

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ikang.libmvi.base.ui.BaseDBViewHoder
import com.ikang.staffapp.R
import com.ikang.staffapp.data.entity.ArticlesBean
import com.ikang.staffapp.databinding.ItemArticleListBinding


class HomeListAdapter :
    BaseQuickAdapter<ArticlesBean, BaseDBViewHoder<ItemArticleListBinding>>(R.layout.item_article_list) {


    override fun convert(helper: BaseDBViewHoder<ItemArticleListBinding>?, item: ArticlesBean?) {
        helper?.getBinding()?.itemData = item
        helper?.getBinding()?.executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil
            .inflate<ItemArticleListBinding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        }
    }
}