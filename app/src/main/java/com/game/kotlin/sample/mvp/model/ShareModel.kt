package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.ShareContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.ShareResponseBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:15
 */
class ShareModel : CommonModel(), ShareContract.Model {
    override fun getShareList(page: Int): Observable<HttpResult<ShareResponseBody>> {
        return RetrofitHelper.service.getShareList(page)
    }

    override fun deleteShareArticle(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.deleteShareArticle(id)
    }
}