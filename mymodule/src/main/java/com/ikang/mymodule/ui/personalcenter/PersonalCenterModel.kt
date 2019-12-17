package com.ikang.staffapp.ui.fragment.me.personalcenter

import androidx.lifecycle.MutableLiveData
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.config.StaticDemoActivity
import com.ikang.staffapp.data.entity.MeListBean
import com.ikang.staffapp.data.entity.MeListItemBean

/**
 * @author ikang-renwei
 * @Date 2019/12/05 16:58
 * @describe
 */
class PersonalCenterModel : BaseViewModel(){

    val meListBean = MutableLiveData<MeListBean>()

    fun getMeListBean(item:Int) {
        launch({
            val mutableListOf = mutableListOf<MeListItemBean>()
            mutableListOf.add(MeListItemBean("","员工号","001",false, StaticDemoActivity.HEAD_JOB_NUMBER))
            mutableListOf.add(MeListItemBean("","职位","产品经理",false, StaticDemoActivity.HEAD_POSITION))
            mutableListOf.add(MeListItemBean("","邮箱","001@ikang.com",false, StaticDemoActivity.HEAD_MAILBOX))
            mutableListOf.add(MeListItemBean("","生日","选择生日",false, StaticDemoActivity.HEAD_BIRTHDAY))
            meListBean.value = MeListBean(
                    "https://ikapp-image.health.ikang.com/2019/11/22/201911221355300410.png",
                    "员工001",
                    1,mutableListOf
            )
        })
    }
}