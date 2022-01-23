package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.KnowledgeContract
import com.game.kotlin.sample.mvp.model.KnowledgeModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:27
 */
class KnowledgePresenter : CommonPresenter<KnowledgeContract.Model, KnowledgeContract.View>(), KnowledgeContract.Presenter {

    override fun createModel(): KnowledgeContract.Model? = KnowledgeModel()

    override fun requestKnowledgeList(page: Int, cid: Int) {
        mModel?.requestKnowledgeList(page, cid)?.ss(mModel, mView) {
            mView?.setKnowledgeList(it.data)
        }
    }

}