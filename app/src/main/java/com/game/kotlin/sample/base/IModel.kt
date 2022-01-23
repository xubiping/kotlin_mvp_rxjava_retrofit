package com.game.kotlin.sample.base

import io.reactivex.disposables.Disposable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 12:00
 */
interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}