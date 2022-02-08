package com.game.kotlin.sample.adapter

import android.app.ActivityOptions
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.game.kotlin.sample.R
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.NavigationBean
import com.game.kotlin.sample.ui.activity.ContentActivity
import com.game.kotlin.sample.utils.CommonUtil
import com.game.kotlin.sample.utils.DisplayManager
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:51
 */
class NavigationAdapter : BaseQuickAdapter<NavigationBean, BaseViewHolder>(R.layout.item_navigation_list),
        LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: NavigationBean) {
        holder.setText(R.id.item_navigation_tv, item.name)
        val flowLayout: TagFlowLayout = holder.getView(R.id.item_navigation_flow_layout)
        val articles: List<Article> = item.articles
        flowLayout.run {
            adapter = object : TagAdapter<Article>(articles) {
                override fun getView(parent: FlowLayout?, position: Int, article: Article?): View? {

                    val tv: TextView = LayoutInflater.from(parent?.context).inflate(
                            R.layout.flow_layout_tv,
                            flowLayout, false
                    ) as TextView

                    article ?: return null

                    val padding: Int = DisplayManager.dip2px(10F)
                    tv.setPadding(padding, padding, padding, padding)
                    tv.text = article.title
                    tv.setTextColor(CommonUtil.randomColor())

                    setOnTagClickListener { view, position, _ ->
                        val options: ActivityOptions = ActivityOptions.makeScaleUpAnimation(
                                view,
                                view.width / 2,
                                view.height / 2,
                                0,
                                0
                        )
                        val data: Article = articles[position]
                        ContentActivity.start(context, data.id, data.title, data.link, options.toBundle())
                        true
                    }
                    return tv
                }
            }
        }
    }
}