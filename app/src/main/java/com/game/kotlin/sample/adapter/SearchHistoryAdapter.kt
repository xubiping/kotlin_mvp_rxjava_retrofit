package com.game.kotlin.sample.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.game.kotlin.sample.R
import com.game.kotlin.sample.mvp.model.bean.SearchHistoryBean

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:56
 */

class SearchHistoryAdapter : BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history) {

        init {
            addChildClickViewIds(R.id.iv_clear)
        }

        override fun convert(holder: BaseViewHolder, item: SearchHistoryBean) {
            holder.setText(R.id.tv_search_key, item.key)
        }
    }