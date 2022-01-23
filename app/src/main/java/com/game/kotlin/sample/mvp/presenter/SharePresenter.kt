package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.ShareContract
import com.game.kotlin.sample.mvp.model.ShareModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:39
 */
class SharePresenter : CommonPresenter<ShareModel, ShareContract.View>(), ShareContract.Presenter {

    override fun createModel(): ShareModel? = ShareModel()

    override fun getShareList(page: Int) {
        mModel?.getShareList(page)?.ss(mModel, mView, page == 1) {
            mView?.showShareList(it.data)
        }
    }

    override fun deleteShareArticle(id: Int) {
        mModel?.deleteShareArticle(id)?.ss(mModel, mView, true) {
            mView?.showDeleteArticle(true)
        }
    }

}