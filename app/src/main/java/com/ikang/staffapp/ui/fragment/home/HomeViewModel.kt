package com.ikang.staffapp.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.event.Message
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.staffapp.data.entity.ArticlesBean
import com.ikang.staffapp.data.entity.BannerBean
import com.ikang.staffapp.data.entity.HomeListBean


/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
class HomeViewModel : BaseViewModel() {



    val mBanners = MutableLiveData<List<BannerBean>>()
    val projectData = MutableLiveData<HomeListBean>()


    fun onClickALogin() {
        defUI.msgEvent.postValue(Message(navigationToLoginActivity))
    }

    fun getBanner() {
        launch({
            val mutableListOf = mutableListOf<BannerBean>()
            for (index in 1..3) {
                mutableListOf.add(
                    BannerBean(
                        "区块链养狗领取 百度莱茨狗区块链养狗领取 百度莱茨狗",
                        "https://ikapp-image.health.ikang.com/2019/11/22/201911221355300410.png"
                    )
                )
            }
            mBanners.value = mutableListOf
        })
    }

    fun getHomeList(page: Int) {
        launch({
            val mutableListOf = mutableListOf<ArticlesBean>()

            for (index in 1..10) {
                mutableListOf.add(
                    ArticlesBean(
                        "区块链养狗领取 百度莱茨狗区块链养狗领取 百度莱茨狗",
                        " 百度莱茨狗区块链养狗领取 百度莱茨狗",
                        "https://ikapp-image.health.ikang.com/2019/11/22/201911221355300410.png"
                    )
                )

            }
            val homeListBean = HomeListBean(page, mutableListOf)
            projectData.value = homeListBean
        })
    }

    companion object {
        const val navigationToLoginActivity: Int = 1

    }
}