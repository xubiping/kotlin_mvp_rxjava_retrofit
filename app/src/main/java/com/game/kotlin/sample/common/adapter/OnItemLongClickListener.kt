package com.game.kotlin.sample.common.adapter

/**
 * Description: Adapter条目的长按事件
 * @author:  xubp
 * @date :   2022/1/23 11:39
 */
@Deprecated("")
interface OnItemLongClickListener {

    fun onItemLongClick(obj: Any?, position: Int): Boolean

}