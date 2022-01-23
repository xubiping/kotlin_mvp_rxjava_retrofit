package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.SquareContract
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:15
 */
class SquareModel : CommonModel(), SquareContract.Model {
    override fun getSquareList(page: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getSquareList(page)
    }
}