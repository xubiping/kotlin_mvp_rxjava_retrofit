package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.BaseListResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.UserScoreBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:34
 */
interface ScoreContract {

    interface View : IView {
        fun showUserScoreList(body: BaseListResponseBody<UserScoreBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getUserScoreList(page: Int)
    }

    interface Model : IModel {
        fun getUserScoreList(page: Int): Observable<HttpResult<BaseListResponseBody<UserScoreBean>>>
    }

}