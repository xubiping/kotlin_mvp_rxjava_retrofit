package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.TodoContract
import com.game.kotlin.sample.mvp.model.bean.AllTodoResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.TodoResponseBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:17
 */
class TodoModel : BaseModel(), TodoContract.Model {

    override fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>> {
        return RetrofitHelper.service.getTodoList(type)
    }

    override fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getNoTodoList(page, type)
    }

    override fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getDoneList(page, type)
    }

    override fun deleteTodoById(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.deleteTodoById(id)
    }

    override fun updateTodoById(id: Int, status: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodoById(id, status)
    }

}