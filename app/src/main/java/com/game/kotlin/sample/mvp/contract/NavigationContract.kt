package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.NavigationBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:40
 */
interface NavigationContract {

    interface View : IView {
        fun setNavigationData(list: List<NavigationBean>)
    }

    interface Presenter : IPresenter<View> {
        fun requestNavigationList()
    }

    interface Model : IModel {
        fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>>
    }

}