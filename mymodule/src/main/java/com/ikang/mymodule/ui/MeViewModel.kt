package com.ikang.staffapp.ui.fragment.me

import androidx.lifecycle.MutableLiveData
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.config.StaticDemoActivity
import com.ikang.staffapp.data.entity.MeListBean
import com.ikang.staffapp.data.entity.MeListItemBean

/**
 * @author ikang-renwei
 * @Date 2019/12/04 17:30
 * @describe
 */

class MeViewModel :BaseViewModel(){

    val meListBean = MutableLiveData<MeListBean>()

    fun getMeListBean(item:Int) {
        launch({
            val mutableListOf = mutableListOf<MeListItemBean>()
            mutableListOf.add(MeListItemBean("","收藏","001",true, StaticDemoActivity.ME_COLLECT))
            mutableListOf.add(MeListItemBean("","我的流程","产品经理",true, StaticDemoActivity.ME_PROCEDURE))
            mutableListOf.add(MeListItemBean("","设置","001@ikang.com",true, StaticDemoActivity.ME_SETTING))
            meListBean.value = MeListBean(
                    "https://ikapp-image.health.ikang.com/2019/11/22/201911221355300410.png",
                    "员工001",
                    1,mutableListOf
            )
        })
    }
}