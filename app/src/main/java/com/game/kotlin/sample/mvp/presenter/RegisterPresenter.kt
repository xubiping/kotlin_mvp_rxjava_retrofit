package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.RegisterContract
import com.game.kotlin.sample.mvp.model.RegisterModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:35
 */
class RegisterPresenter : BasePresenter<RegisterContract.Model, RegisterContract.View>(), RegisterContract.Presenter {

    override fun createModel(): RegisterContract.Model? = RegisterModel()

    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        mModel?.registerWanAndroid(username, password, repassword)?.ss(mModel, mView) {
            mView?.apply {
                if (it.errorCode != 0) {
                    registerFail()
                } else {
                    registerSuccess(it.data)
                }
            }
        }
    }


}