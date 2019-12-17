package com.ikang.staffapp.ui.fragment.me

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ikang.libmvi.base.ui.BaseDBViewHoder
import com.ikang.mymodule.R
import com.ikang.mymodule.databinding.ItemMeListBinding
import com.ikang.staffapp.data.entity.MeListItemBean

/**
 * @author ikang-renwei
 * @Date 2019/12/04 13:41
 * @describe 我的布局适配器
 */
class MeListAdapter : BaseQuickAdapter<MeListItemBean,BaseDBViewHoder<ItemMeListBinding>>(R.layout.item_me_list){
    override fun convert(helper: BaseDBViewHoder<ItemMeListBinding>?, item: MeListItemBean?) {
        helper?.getBinding()?.itemData = item
        helper?.getBinding()?.executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        Log.e("","")
        val binding = DataBindingUtil.inflate<ItemMeListBinding>(mLayoutInflater,layoutResId,parent,false)
                ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.BaseQuickAdapter_databinding_support,binding)
        }
    }


}