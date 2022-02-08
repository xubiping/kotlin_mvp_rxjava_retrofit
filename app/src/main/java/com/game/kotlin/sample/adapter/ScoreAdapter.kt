package com.game.kotlin.sample.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.game.kotlin.sample.R
import com.game.kotlin.sample.mvp.model.bean.UserScoreBean

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:55
 */
class ScoreAdapter : BaseQuickAdapter<UserScoreBean, BaseViewHolder>(R.layout.item_socre_list), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: UserScoreBean) {
        holder.setText(R.id.tv_reason, item.reason)
                .setText(R.id.tv_desc, item.desc)
                .setText(R.id.tv_score, "+${item.coinCount}")
    }
}