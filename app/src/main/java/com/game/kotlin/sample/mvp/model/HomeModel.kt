package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.HomeContract
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.Banner
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:36
 */
class HomeModel : CommonModel(), HomeContract.Model {

    override fun requestBanner(): Observable<HttpResult<List<Banner>>> {
        return RetrofitHelper.service.getBanners()
    }

    override fun requestTopArticles(): Observable<HttpResult<MutableList<Article>>> {
        return RetrofitHelper.service.getTopArticles()
    }

    override fun requestArticles(num: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getArticles(num)
    }

}