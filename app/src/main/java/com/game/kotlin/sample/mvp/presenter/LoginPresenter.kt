package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.LoginContract
import com.game.kotlin.sample.mvp.model.LoginModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:29
 */
class LoginPresenter : BasePresenter<LoginContract.Model, LoginContract.View>(), LoginContract.Presenter {

    override fun createModel(): LoginContract.Model? = LoginModel()

    override fun loginWanAndroid(username: String, password: String) {
        mModel?.loginWanAndroid(username, password)?.ss(mModel, mView) {
            mView?.loginSuccess(it.data)
        }
    }

}