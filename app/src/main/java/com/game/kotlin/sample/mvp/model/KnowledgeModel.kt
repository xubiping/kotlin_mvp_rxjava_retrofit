package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.KnowledgeContract
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:36
 */
class KnowledgeModel : CommonModel(), KnowledgeContract.Model {

    override fun requestKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getKnowledgeList(page, cid)
    }

}