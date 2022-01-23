package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.WeChatContract
import com.game.kotlin.sample.mvp.model.WeChatModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:43
 */
class WeChatPresenter : BasePresenter<WeChatContract.Model, WeChatContract.View>(), WeChatContract.Presenter {

    override fun createModel(): WeChatContract.Model? = WeChatModel()

    override fun getWXChapters() {
        mModel?.getWXChapters()?.ss(mModel, mView) {
            mView?.showWXChapters(it.data)
        }
    }

}