package com.game.kotlin.sample.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.game.kotlin.sample.R
import com.game.kotlin.sample.mvp.model.bean.CollectionArticle
import com.game.kotlin.sample.utils.ImageLoader

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:48
 */
class CollectAdapter : BaseQuickAdapter<CollectionArticle, BaseViewHolder>(R.layout.item_collect_list), LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_like)
    }

    override fun convert(holder: BaseViewHolder, item: CollectionArticle) {
        val authorStr = when {
            item.author.isNotEmpty() -> item.author
            else -> context.getString(R.string.anonymous)
        }

        holder.setText(R.id.tv_article_title, Html.fromHtml(item.title))
                .setText(R.id.tv_article_author, authorStr)
                .setText(R.id.tv_article_date, item.niceDate)
                .setImageResource(R.id.iv_like, R.drawable.ic_like)

        holder.setText(R.id.tv_article_chapterName, item.chapterName)
        if (item.envelopePic.isNotEmpty()) {
            holder.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.VISIBLE
            ImageLoader.load(context, item.envelopePic, holder.getView(R.id.iv_article_thumbnail))
        } else {
            holder.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.GONE
        }
    }
}