package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.ProjectListContract
import com.game.kotlin.sample.mvp.model.ProjectListModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:32
 */
class ProjectListPresenter : CommonPresenter<ProjectListContract.Model, ProjectListContract.View>(), ProjectListContract.Presenter {

    override fun createModel(): ProjectListContract.Model? = ProjectListModel()

    override fun requestProjectList(page: Int, cid: Int) {
        mModel?.requestProjectList(page, cid)?.ss(mModel, mView, page == 1) {
            mView?.setProjectList(it.data)
        }
    }

}