package com.game.kotlin.sample.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.ProjectAdapter
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseMvpListFragment
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showSnackMsg
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.ProjectListContract
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.presenter.ProjectListPresenter
import com.game.kotlin.sample.ui.activity.ContentActivity
import com.game.kotlin.sample.ui.activity.LoginActivity
import com.game.kotlin.sample.utils.NetWorkUtil
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:16
 */
class ProjectListFragment : BaseMvpListFragment<ProjectListContract.View, ProjectListContract.Presenter>(),
        ProjectListContract.View {

    companion object {
        fun getInstance(cid: Int): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * cid
     */
    private var cid: Int = -1

    /**
     * ProjectAdapter
     */
    private val mAdapter: ProjectAdapter by lazy {
        ProjectAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun createPresenter(): ProjectListContract.Presenter = ProjectListPresenter()

    override fun initView(view: View) {
        super.initView(view)

        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: -1

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
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestProjectList(1, cid)
    }

    override fun onRefreshList() {
        mPresenter?.requestProjectList(1, cid)
    }

    override fun onLoadMoreList() {
        mPresenter?.requestProjectList(pageNum + 1, cid)
    }

    override fun setProjectList(articles: ArticleResponseBody) {
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
            R.id.item_project_list_like_iv -> {
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