package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.ShareArticleContract
import com.game.kotlin.sample.mvp.model.ShareArticleModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:38
 */
class ShareArticlePresenter : BasePresenter<ShareArticleModel, ShareArticleContract.View>(), ShareArticleContract.Presenter {

    override fun createModel(): ShareArticleModel? = ShareArticleModel()

    override fun shareArticle() {
        val title = mView?.getArticleTitle().toString()
        val link = mView?.getArticleLink().toString()

        if (title.isEmpty()) {
            mView?.showMsg("文章标题不能为空")
            return
        }
        if (link.isEmpty()) {
            mView?.showMsg("文章链接不能为空")
            return
        }
        val map = mutableMapOf<String, Any>()
        map["title"] = title
        map["link"] = link
        mModel?.shareArticle(map)?.ss(mModel, mView, true) {
            mView?.showShareArticle(true)
        }

    }

}