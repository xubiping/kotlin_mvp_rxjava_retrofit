package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.SquareContract
import com.game.kotlin.sample.mvp.model.SquareModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:40
 */
class SquarePresenter : CommonPresenter<SquareModel, SquareContract.View>(), SquareContract.Presenter {

    override fun createModel(): SquareModel? = SquareModel()

    override fun getSquareList(page: Int) {
        mModel?.getSquareList(page)?.ss(mModel, mView, page == 0) {
            mView?.showSquareList(it.data)
        }
    }

}