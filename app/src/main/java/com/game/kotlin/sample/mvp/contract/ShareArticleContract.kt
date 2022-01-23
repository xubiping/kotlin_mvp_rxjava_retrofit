package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:36
 */
interface ShareArticleContract {

    interface View : IView {
        fun getArticleTitle(): String
        fun getArticleLink(): String

        fun showShareArticle(success: Boolean)
    }

    interface Presenter : IPresenter<View> {
        fun shareArticle()
    }

    interface Model : IModel {
        fun shareArticle(map: MutableMap<String, Any>): Observable<HttpResult<Any>>
    }

}