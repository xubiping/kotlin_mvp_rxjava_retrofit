package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.ScoreContract
import com.game.kotlin.sample.mvp.model.bean.BaseListResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.UserScoreBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:12
 */
class ScoreModel : BaseModel(), ScoreContract.Model {
    override fun getUserScoreList(page: Int): Observable<HttpResult<BaseListResponseBody<UserScoreBean>>> {
        return RetrofitHelper.service.getUserScoreList(page)
    }
}