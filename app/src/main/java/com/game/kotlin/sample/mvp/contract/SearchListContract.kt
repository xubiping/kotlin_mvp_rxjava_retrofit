package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:35
 */
interface SearchListContract {

    interface View : CommonContract.View {

        fun showArticles(articles: ArticleResponseBody)

        fun scrollToTop()

    }

    interface Presenter : CommonContract.Presenter<View> {

        fun queryBySearchKey(page: Int, key: String)

    }

    interface Model : CommonContract.Model {

        fun queryBySearchKey(page: Int, key: String): Observable<HttpResult<ArticleResponseBody>>

    }

}