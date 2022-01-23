package com.game.kotlin.sample.mvp.contract

import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IPresenter
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.mvp.model.bean.AllTodoResponseBody
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.TodoResponseBody
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 16:40
 */
interface TodoContract {

    interface View : IView {

        fun showNoTodoList(todoResponseBody: TodoResponseBody)

        fun showDeleteSuccess(success: Boolean)

        fun showUpdateSuccess(success: Boolean)

    }

    interface Presenter : IPresenter<View> {

        fun getAllTodoList(type: Int)

        fun getNoTodoList(page: Int, type: Int)

        fun getDoneList(page: Int, type: Int)

        fun deleteTodoById(id: Int)

        fun updateTodoById(id: Int, status: Int)

    }

    interface Model : IModel {

        fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>>

        fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>>

        fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>>

        fun deleteTodoById(id: Int): Observable<HttpResult<Any>>

        fun updateTodoById(id: Int, status: Int): Observable<HttpResult<Any>>

    }

}