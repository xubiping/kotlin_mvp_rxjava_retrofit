package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.NavigationContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.NavigationBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:08
 */
class NavigationModel : BaseModel(), NavigationContract.Model {

    override fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>> {
        return RetrofitHelper.service.getNavigationList()
    }

}