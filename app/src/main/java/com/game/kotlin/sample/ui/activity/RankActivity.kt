package com.game.kotlin.sample.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.RankAdapter
import com.game.kotlin.sample.base.BaseMvpSwipeBackActivity
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.mvp.contract.RankContract
import com.game.kotlin.sample.mvp.model.bean.BaseListResponseBody
import com.game.kotlin.sample.mvp.model.bean.CoinInfoBean
import com.game.kotlin.sample.mvp.presenter.RankPresenter
import com.game.kotlin.sample.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 21:44
 */
/**
 * 排行榜页面
 */
class RankActivity : BaseMvpSwipeBackActivity<RankContract.View, RankContract.Presenter>(), RankContract.View {

    /**
     * 每页数据的个数
     */
    private var pageSize = 20

    /**
     * PageNum
     */
    private var pageNum = 1

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    private val rankAdapter: RankAdapter by lazy {
        RankAdapter()
    }

    override fun createPresenter(): RankContract.Presenter = RankPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_rank

    override fun showLoading() {
        // swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        mLayoutStatusView = multiple_status_view
        toolbar.run {
            title = getString(R.string.score_list)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNum = 1
            mPresenter?.getRankList(pageNum)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@RankActivity)
            adapter = rankAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }
        rankAdapter.run {
            loadMoreModule.setOnLoadMoreListener {
                pageNum++
                swipeRefreshLayout.isRefreshing = false
                mPresenter?.getRankList(pageNum)
            }
        }
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getRankList(1)
    }

    override fun showRankList(body: BaseListResponseBody<CoinInfoBean>) {
        rankAdapter.setNewOrAddData(pageNum == 1, body.datas)
        if (rankAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }
}