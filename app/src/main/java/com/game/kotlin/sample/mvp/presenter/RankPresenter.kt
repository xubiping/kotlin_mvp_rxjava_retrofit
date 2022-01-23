package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.RankContract
import com.game.kotlin.sample.mvp.model.RankModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:34
 */
class RankPresenter : BasePresenter<RankContract.Model, RankContract.View>(), RankContract.Presenter {

    override fun createModel(): RankContract.Model? = RankModel()

    override fun getRankList(page: Int) {
        mModel?.getRankList(page)?.ss(mModel, mView) {
            mView?.showRankList(it.data)
        }
    }
}