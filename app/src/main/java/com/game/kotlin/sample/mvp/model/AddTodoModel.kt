package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.AddTodoContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:31
 */
class AddTodoModel : BaseModel(), AddTodoContract.Model {

    override fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.addTodo(map)
    }

    override fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodo(id, map)
    }

}
