package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.NavigationContract
import com.game.kotlin.sample.mvp.model.NavigationModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:31
 */
class NavigationPresenter : BasePresenter<NavigationContract.Model, NavigationContract.View>(), NavigationContract.Presenter {

    override fun createModel(): NavigationContract.Model? = NavigationModel()

    override fun requestNavigationList() {
        mModel?.requestNavigationList()?.ss(mModel, mView) {
            mView?.setNavigationData(it.data)
        }
    }

}