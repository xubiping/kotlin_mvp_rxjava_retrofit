package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.RegisterContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:11
 */
class RegisterModel : BaseModel(), RegisterContract.Model {

    override fun registerWanAndroid(username: String, password: String, repassword: String): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.service.registerWanAndroid(username, password, repassword)
    }

}