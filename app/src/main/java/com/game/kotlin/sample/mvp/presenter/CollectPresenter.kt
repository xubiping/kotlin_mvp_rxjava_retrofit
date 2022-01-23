package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.CollectContract
import com.game.kotlin.sample.mvp.model.CollectModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:22
 */
class CollectPresenter : BasePresenter<CollectContract.Model, CollectContract.View>(), CollectContract.Presenter {

    override fun createModel(): CollectContract.Model? = CollectModel()

    override fun getCollectList(page: Int) {
        mModel?.getCollectList(page)?.ss(mModel, mView, page == 0) {
            mView?.setCollectList(it.data)
        }
    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        mModel?.removeCollectArticle(id, originId)?.ss(mModel, mView) {
            mView?.showRemoveCollectSuccess(true)
        }
    }

}