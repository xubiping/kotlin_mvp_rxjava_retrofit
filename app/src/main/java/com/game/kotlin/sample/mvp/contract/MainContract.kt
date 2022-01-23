package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.UserInfoBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:26
 */
interface MainContract {

    interface View : IView {
        fun showLogoutSuccess(success: Boolean)
        fun showUserInfo(bean: UserInfoBody)
    }

    interface Presenter : IPresenter<View> {
        fun logout()
        fun getUserInfo()
    }

    interface Model : IModel {
        fun logout(): Observable<HttpResult<Any>>
        fun getUserInfo(): Observable<HttpResult<UserInfoBody>>
    }

}