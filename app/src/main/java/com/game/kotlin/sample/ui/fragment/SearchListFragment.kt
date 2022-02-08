package com.game.kotlin.sample.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.HomeAdapter
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseMvpListFragment
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.event.ColorEvent
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showSnackMsg
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.SearchListContract
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.presenter.SearchListPresenter
import com.game.kotlin.sample.ui.activity.ContentActivity
import com.game.kotlin.sample.ui.activity.LoginActivity
import com.game.kotlin.sample.utils.NetWorkUtil
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_search_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:18
 */
class SearchListFragment : BaseMvpListFragment<SearchListContract.View, SearchListContract.Presenter>(),
        SearchListContract.View {

    companion object {
        fun getInstance(bundle: Bundle): SearchListFragment {
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mKey = ""

    /**
     * Adapter
     */
    private val mAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_search_list

    override fun createPresenter(): SearchListContract.Presenter = SearchListPresenter()

    override fun useEventBus(): Boolean = true

    override fun initView(view: View) {
        super.initView(view)

        mKey = arguments?.getString(Constant.SEARCH_KEY, "") ?: ""

        recyclerView.adapter = mAdapter

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
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
        mPresenter?.queryBySearchKey(0, mKey)
    }

    override fun onRefreshList() {
        mPresenter?.queryBySearchKey(0, mKey)
    }

    override fun onLoadMoreList() {
        mPresenter?.queryBySearchKey(pageNum, mKey)
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
        }
    }

    override fun showArticles(articles: ArticleResponseBody) {
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
    private fun itemClick(item: Article) {
        ContentActivity.start(activity, item.id, item.title, item.link)
    }

    /**
     * Item Child Click
     * @param item Article
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: Article, view: View, position: Int) {
        when (view.id) {
            R.id.iv_like -> {
                if (isLogin) {
                    if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                        showSnackMsg(resources.getString(R.string.no_network))
                        return
                    }
                    val collect = item.collect
                    item.collect = !collect
                    mAdapter.setData(position, item)
                    if (collect) {
                        mPresenter?.cancelCollectArticle(item.id)
                    } else {
                        mPresenter?.addCollectArticle(item.id)
                    }
                } else {
                    Intent(activity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    showToast(resources.getString(R.string.login_tint))
                }
            }
        }
    }
}