package com.game.kotlin.sample.common.adapter

/**
 * desc: 多布局条目类型
 * @author:  xubp
 * @date :   2022/1/23 11:34
 */
@Deprecated("")
interface MultipleType<in T> {
    fun getLayoutId(item: T, position: Int): Int
}