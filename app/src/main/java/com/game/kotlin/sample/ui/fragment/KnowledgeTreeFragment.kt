package com.game.kotlin.sample.ui.fragment

import android.content.Intent
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.KnowledgeTreeAdapter
import com.game.kotlin.sample.base.BaseMvpListFragment
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.mvp.contract.KnowledgeTreeContract
import com.game.kotlin.sample.mvp.model.bean.KnowledgeTreeBody
import com.game.kotlin.sample.mvp.presenter.KnowledgeTreePresenter
import com.game.kotlin.sample.ui.activity.KnowledgeActivity
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:05
 */
class KnowledgeTreeFragment : BaseMvpListFragment<KnowledgeTreeContract.View, KnowledgeTreeContract.Presenter>(),
        KnowledgeTreeContract.View {

    companion object {
        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    /**
     * Adapter
     */
    private val mAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter()
    }

    override fun createPresenter(): KnowledgeTreeContract.Presenter = KnowledgeTreePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)

        recyclerView.adapter = mAdapter

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as KnowledgeTreeBody
                itemClick(item)
            }
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeTree()
    }

    override fun onRefreshList() {
        mPresenter?.requestKnowledgeTree()
    }

    override fun onLoadMoreList() {
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
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

    override fun setKnowledgeTree(lists: List<KnowledgeTreeBody>) {
        mAdapter.setList(lists)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * Item Click
     */
    private fun itemClick(item: KnowledgeTreeBody) {
        Intent(activity, KnowledgeActivity::class.java).run {
            putExtra(Constant.CONTENT_TITLE_KEY, item.name)
            putExtra(Constant.CONTENT_DATA_KEY, item)
            startActivity(this)
        }
    }
}