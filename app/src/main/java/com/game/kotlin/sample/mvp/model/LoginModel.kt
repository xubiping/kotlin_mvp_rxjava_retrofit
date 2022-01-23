package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.LoginContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:05
 */
class LoginModel : BaseModel(), LoginContract.Model {

    override fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.service.loginWanAndroid(username, password)
    }

}