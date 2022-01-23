package com.game.kotlin.sample.mvp.model.bean

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:28
 */
class TodoDataBean(var headerName: String = "", var todoBean: TodoBean? = null) : SectionEntity {

    override val isHeader: Boolean
        get() = headerName.isNotEmpty()

}