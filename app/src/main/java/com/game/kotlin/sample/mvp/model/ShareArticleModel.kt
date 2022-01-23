package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.ShareArticleContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:14
 */
class ShareArticleModel : BaseModel(), ShareArticleContract.Model {
    override fun shareArticle(map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.shareArticle(map)
    }
}