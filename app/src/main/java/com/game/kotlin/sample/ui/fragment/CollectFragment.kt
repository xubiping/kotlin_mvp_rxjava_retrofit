package com.game.kotlin.sample.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.CollectAdapter
import com.game.kotlin.sample.base.BaseMvpListFragment
import com.game.kotlin.sample.event.ColorEvent
import com.game.kotlin.sample.event.RefreshHomeEvent
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.CollectContract
import com.game.kotlin.sample.mvp.model.bean.BaseListResponseBody
import com.game.kotlin.sample.mvp.model.bean.CollectionArticle
import com.game.kotlin.sample.mvp.presenter.CollectPresenter
import com.game.kotlin.sample.ui.activity.ContentActivity
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:02
 */
class CollectFragment : BaseMvpListFragment<CollectContract.View, CollectContract.Presenter>(), CollectContract.View {

    companion object {
        fun getInstance(bundle: Bundle): CollectFragment {
            val fragment = CollectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * CollectAdapter
     */
    private val mAdapter: CollectAdapter by lazy {
        CollectAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun createPresenter(): CollectContract.Presenter = CollectPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_collect

    override fun useEventBus(): Boolean = true

    override fun initView(view: View) {
        super.initView(view)

        recyclerView.adapter = mAdapter

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as CollectionArticle
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as CollectionArticle
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener(onRequestLoadMoreListener)
        }

        floating_action_btn.setOnClickListener {
            scrollToTop()
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getCollectList(0)
    }

    override fun onRefreshList() {
        mPresenter?.getCollectList(0)
    }

    override fun onLoadMoreList() {
        mPresenter?.getCollectList(pageNum)
    }

    override fun showRemoveCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun setCollectList(articles: BaseListResponseBody<CollectionArticle>) {
        mAdapter.setNewOrAddData(pageNum == 0, articles.datas)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(event.color)
        }
    }

    /**
     * Item Click
     */
    private fun itemClick(item: CollectionArticle) {
        ContentActivity.start(activity, item.id, item.title, item.link)
    }

    /**
     * Item Child Click
     * @param item Article
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: CollectionArticle, view: View, position: Int) {
        when (view.id) {
            R.id.iv_like -> {
                mAdapter.removeAt(position)
                mPresenter?.removeCollectArticle(item.id, item.originId)
            }
        }
    }
}