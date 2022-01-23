package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.ScoreContract
import com.game.kotlin.sample.mvp.model.ScoreModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:35
 */
class ScorePresenter : BasePresenter<ScoreContract.Model, ScoreContract.View>(), ScoreContract.Presenter {

    override fun createModel(): ScoreContract.Model? = ScoreModel()

    override fun getUserScoreList(page: Int) {
        mModel?.getUserScoreList(page)?.ss(mModel, mView) {
            mView?.showUserScoreList(it.data)
        }
    }
}