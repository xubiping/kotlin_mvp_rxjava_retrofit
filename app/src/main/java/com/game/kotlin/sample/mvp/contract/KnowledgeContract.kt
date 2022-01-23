package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:21
 */
interface KnowledgeContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setKnowledgeList(articles: ArticleResponseBody)

    }

    interface Presenter : CommonContract.Presenter<View> {

        fun requestKnowledgeList(page: Int, cid: Int)

    }

    interface Model : CommonContract.Model {

        fun requestKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>>

    }

}