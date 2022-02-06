package com.game.kotlin.sample.ui.fragment

import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.WeChatPagerAdapter
import com.game.kotlin.sample.base.BaseMvpFragment
import com.game.kotlin.sample.databinding.FragmentRefreshLayoutBinding
import com.game.kotlin.sample.databinding.FragmentWechatBinding
import com.game.kotlin.sample.event.ColorEvent
import com.game.kotlin.sample.mvp.contract.WeChatContract
import com.game.kotlin.sample.mvp.model.bean.WXChapterBean
import com.game.kotlin.sample.mvp.presenter.WeChatPresenter
import com.game.kotlin.sample.utils.SettingUtil
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @description: 公众号
 * @author:  xubp
 * @date :   2022/2/5 20:03
 */
class WeChatFragment : BaseMvpFragment<WeChatContract.View, WeChatContract.Presenter>(), WeChatContract.View {
    lateinit var binding: FragmentRefreshLayoutBinding
    lateinit var bindingWeChat: FragmentWechatBinding
    companion object {
        fun getInstance(): WeChatFragment = WeChatFragment()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<WXChapterBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: WeChatPagerAdapter by lazy {
        WeChatPagerAdapter(datas, childFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_wechat

    override fun createPresenter(): WeChatContract.Presenter = WeChatPresenter()

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentRefreshLayoutBinding.inflate(layoutInflater)
        bindingWeChat = FragmentWechatBinding.inflate(layoutInflater)
        mLayoutStatusView = binding.multipleStatusView
        bindingWeChat.viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(bindingWeChat.tabLayout))
        }

        bindingWeChat.tabLayout.run {
            setupWithViewPager(bindingWeChat.viewPager)
            // TabLayoutHelper.setUpIndicatorWidth(tabLayout)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(bindingWeChat.viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        refreshColor(ColorEvent(true))

    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun lazyLoad() {
        mPresenter?.getWXChapters()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                bindingWeChat.tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun doReConnected() {
        if (datas.size == 0) {
            super.doReConnected()
        }
    }

    override fun showWXChapters(chapters: MutableList<WXChapterBean>) {
        chapters.let {
            datas.addAll(it)
            doAsync {
                Thread.sleep(10)
                uiThread {
                    bindingWeChat.viewPager.run {
                        adapter = viewPagerAdapter
                        offscreenPageLimit = datas.size
                    }
                }
            }
        }
        if (chapters.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                bindingWeChat.viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) {
            return
        }
        val fragment: KnowledgeFragment = viewPagerAdapter.getItem(bindingWeChat.viewPager.currentItem) as KnowledgeFragment
        fragment.scrollToTop()
    }

}