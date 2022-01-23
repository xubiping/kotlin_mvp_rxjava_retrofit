package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.ProjectListContract
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:08
 */
class ProjectListModel : CommonModel(), ProjectListContract.Model {

    override fun requestProjectList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getProjectList(page, cid)
    }

}