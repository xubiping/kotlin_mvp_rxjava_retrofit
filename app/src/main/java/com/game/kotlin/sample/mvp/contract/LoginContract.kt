package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:25
 */
interface LoginContract {

    interface View : IView {

        fun loginSuccess(data: LoginData)

        fun loginFail()

    }

    interface Presenter : IPresenter<View> {

        fun loginWanAndroid(username: String, password: String)

    }

    interface Model : IModel {

        fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>>

    }

}