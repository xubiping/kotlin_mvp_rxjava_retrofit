package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.BaseListResponseBody
import com.game.kotlin.sample.mvp.model.bean.CoinInfoBean
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:32
 */
interface RankContract {

    interface View : IView {
        fun showRankList(body: BaseListResponseBody<CoinInfoBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getRankList(page: Int)
    }

    interface Model : IModel {
        fun getRankList(page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>>
    }

}