package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.ext.sss
import com.game.kotlin.sample.mvp.contract.MainContract
import com.game.kotlin.sample.mvp.model.MainModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:30
 */
class MainPresenter : BasePresenter<MainContract.Model, MainContract.View>(), MainContract.Presenter {

    override fun createModel(): MainContract.Model? = MainModel()

    override fun logout() {
        mModel?.logout()?.ss(mModel, mView) {
            mView?.showLogoutSuccess(success = true)
        }
    }

    override fun getUserInfo() {
        mModel?.getUserInfo()?.sss(mView, false, {
            mView?.showUserInfo(it.data)
        }, {})
    }

}