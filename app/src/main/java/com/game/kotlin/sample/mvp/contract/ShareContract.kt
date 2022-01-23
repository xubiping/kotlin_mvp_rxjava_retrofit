package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.ShareResponseBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:37
 */
interface ShareContract {

    interface View : CommonContract.View {
        fun showShareList(body: ShareResponseBody)
        fun showDeleteArticle(success: Boolean)
    }

    interface Presenter : CommonContract.Presenter<View> {
        fun getShareList(page: Int)
        fun deleteShareArticle(id: Int)
    }

    interface Model : CommonContract.Model {
        fun getShareList(page: Int): Observable<HttpResult<ShareResponseBody>>
        fun deleteShareArticle(id: Int): Observable<HttpResult<Any>>
    }

}