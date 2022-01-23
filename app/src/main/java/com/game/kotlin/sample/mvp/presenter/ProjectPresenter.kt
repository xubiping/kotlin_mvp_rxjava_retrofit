package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.ProjectContract
import com.game.kotlin.sample.mvp.model.ProjectModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:33
 */
class ProjectPresenter : BasePresenter<ProjectContract.Model, ProjectContract.View>(), ProjectContract.Presenter {

    override fun createModel(): ProjectContract.Model? = ProjectModel()

    override fun requestProjectTree() {
        mModel?.requestProjectTree()?.ss(mModel, mView) {
            mView?.setProjectTree(it.data)
        }
    }

}