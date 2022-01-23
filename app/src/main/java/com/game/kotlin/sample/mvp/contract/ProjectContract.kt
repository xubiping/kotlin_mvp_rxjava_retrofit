package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:30
 */
interface ProjectContract {

    interface View : IView {

        fun scrollToTop()

        fun setProjectTree(list: List<ProjectTreeBean>)

    }

    interface Presenter : IPresenter<View> {

        fun requestProjectTree()

    }

    interface Model : IModel {
        fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>
    }

}