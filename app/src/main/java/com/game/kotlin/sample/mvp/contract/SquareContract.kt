package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:38
 */
interface SquareContract {

    interface View : CommonContract.View {
        fun scrollToTop()
        fun showSquareList(body: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View> {
        fun getSquareList(page: Int)
    }

    interface Model : CommonContract.Model {
        fun getSquareList(page: Int): Observable<HttpResult<ArticleResponseBody>>
    }

}