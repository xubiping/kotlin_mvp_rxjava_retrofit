package com.game.kotlin.sample.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.game.kotlin.sample.R
import com.game.kotlin.sample.mvp.model.bean.KnowledgeTreeBody

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:49
 */

class KnowledgeTreeAdapter : BaseQuickAdapter<KnowledgeTreeBody, BaseViewHolder>(R.layout.item_knowledge_tree_list),
            LoadMoreModule {

        override fun convert(holder: BaseViewHolder, item: KnowledgeTreeBody) {
            holder.setText(R.id.title_first, item.name)
            item.children.let {
                holder.setText(
                        R.id.title_second,
                        it.joinToString("    ", transform = { child ->
                            Html.fromHtml(child.name)
                        })
                )
            }
        }
    }