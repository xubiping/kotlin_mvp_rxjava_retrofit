package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.KnowledgeTreeContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:04
 */
class KnowledgeTreeModel : BaseModel(), KnowledgeTreeContract.Model {

    override fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>> {
        return RetrofitHelper.service.getKnowledgeTree()
    }

}