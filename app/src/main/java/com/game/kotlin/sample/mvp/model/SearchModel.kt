package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.SearchContract
import com.game.kotlin.sample.mvp.model.bean.HotSearchBean
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:13
 */
class SearchModel : BaseModel(), SearchContract.Model {

    override fun getHotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>> {
        return RetrofitHelper.service.getHotSearchData()
    }

}