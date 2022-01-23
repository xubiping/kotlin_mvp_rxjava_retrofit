package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.ProjectContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:09
 */
class ProjectModel : BaseModel(), ProjectContract.Model {

    override fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>> {
        return RetrofitHelper.service.getProjectTree()
    }

}