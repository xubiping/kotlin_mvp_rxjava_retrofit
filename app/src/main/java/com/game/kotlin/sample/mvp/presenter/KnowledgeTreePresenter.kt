package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.KnowledgeTreeContract
import com.game.kotlin.sample.mvp.model.KnowledgeTreeModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:28
 */
class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.Model, KnowledgeTreeContract.View>(), KnowledgeTreeContract.Presenter {

    override fun createModel(): KnowledgeTreeContract.Model? = KnowledgeTreeModel()

    override fun requestKnowledgeTree() {
        mModel?.requestKnowledgeTree()?.ss(mModel, mView) {
            mView?.setKnowledgeTree(it.data)
        }
    }

}