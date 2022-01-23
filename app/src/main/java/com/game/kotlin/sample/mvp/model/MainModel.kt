package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.MainContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.UserInfoBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:06
 */
class MainModel : BaseModel(), MainContract.Model {

    override fun logout(): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.logout()
    }

    override fun getUserInfo(): Observable<HttpResult<UserInfoBody>> {
        return RetrofitHelper.service.getUserInfo()
    }

}