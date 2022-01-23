package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.RankContract
import com.game.kotlin.sample.mvp.model.bean.BaseListResponseBody
import com.game.kotlin.sample.mvp.model.bean.CoinInfoBean
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:10
 */
class RankModel : BaseModel(), RankContract.Model {
    override fun getRankList(page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>> {
        return RetrofitHelper.service.getRankList(page)
    }
}