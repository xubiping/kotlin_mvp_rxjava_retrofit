package com.game.kotlin.sample.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.game.kotlin.sample.R
import com.game.kotlin.sample.base.BaseMvpFragment
import com.game.kotlin.sample.event.ColorEvent
import com.game.kotlin.sample.mvp.contract.SystemContract
import com.game.kotlin.sample.mvp.presenter.SystemPresenter
import com.game.kotlin.sample.utils.SettingUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_system.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @description:体系
 * @author:  xubp
 * @date :   2022/2/8 10:30
 */
class SystemFragment : BaseMvpFragment<SystemContract.View, SystemPresenter>(), SystemContract.View {

    companion object {
        fun getInstance(): SystemFragment = SystemFragment()
    }

    private val titleList = mutableListOf<String>()
    private val fragmentList = mutableListOf<Fragment>()
    private val systemPagerAdapter: SystemPagerAdapter by lazy {
        SystemPagerAdapter(childFragmentManager, titleList, fragmentList)
    }

    override fun createPresenter(): SystemPresenter = SystemPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_system

    override fun initView(view: View) {
        super.initView(view)

        titleList.add(getString(R.string.knowledge_system))
        titleList.add(getString(R.string.navigation))
        fragmentList.add(KnowledgeTreeFragment.getInstance())
        fragmentList.add(NavigationFragment.getInstance())

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            adapter = systemPagerAdapter
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        refreshColor(ColorEvent(true))

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun lazyLoad() {
    }

    override fun scrollToTop() {
        if (viewPager.currentItem == 0) {
            (systemPagerAdapter.getItem(0) as KnowledgeTreeFragment).scrollToTop()
        } else if (viewPager.currentItem == 1) {
            (systemPagerAdapter.getItem(1) as NavigationFragment).scrollToTop()
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
                viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    class SystemPagerAdapter(
            fm: FragmentManager,
            private val titleList: MutableList<String>,
            private val fragmentList: MutableList<Fragment>
    ) : FragmentPagerAdapter(fm) {

        override fun getItem(i: Int): Fragment = fragmentList[i]

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence? = titleList[position]

    }

}