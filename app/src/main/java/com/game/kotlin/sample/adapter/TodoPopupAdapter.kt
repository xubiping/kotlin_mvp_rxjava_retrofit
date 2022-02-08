package com.game.kotlin.sample.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.game.kotlin.sample.R
import com.game.kotlin.sample.mvp.model.bean.TodoTypeBean

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:59
 */
class TodoPopupAdapter : BaseQuickAdapter<TodoTypeBean, BaseViewHolder>(R.layout.item_todo_popup_list) {

    override fun convert(helper: BaseViewHolder, item: TodoTypeBean) {
        val tv_popup = helper.getView<TextView>(R.id.tv_popup)
        tv_popup.text = item.name
        if (item.isSelected) {
            tv_popup.setTextColor(context.resources.getColor(R.color.colorAccent))
        } else {
            tv_popup.setTextColor(context.resources.getColor(R.color.common_color))
        }
    }
}