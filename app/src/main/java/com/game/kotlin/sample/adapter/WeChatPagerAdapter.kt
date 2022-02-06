package com.game.kotlin.sample.adapter

import android.text.Html
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.game.kotlin.sample.mvp.model.bean.WXChapterBean
import com.game.kotlin.sample.ui.fragment.KnowledgeFragment

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/5 20:16
 */
class WeChatPagerAdapter(private val list: MutableList<WXChapterBean>, fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(KnowledgeFragment.getInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = Html.fromHtml(list[position].name)

    override fun getItemPosition(`object`: Any): Int = POSITION_NONE


}