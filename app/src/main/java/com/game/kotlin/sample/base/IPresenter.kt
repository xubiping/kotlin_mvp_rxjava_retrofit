package com.game.kotlin.sample.base

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:59
 */
interface IPresenter<in V : IView> {

    /**
     * 绑定 View
     */
    fun attachView(mView: V)

    /**
     * 解绑 View
     */
    fun detachView()

}