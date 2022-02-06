package com.game.kotlin.sample.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.KnowledgeAdapter
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseMvpListFragment
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.databinding.FragmentRefreshLayoutBinding
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showSnackMsg
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.KnowledgeContract
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.presenter.KnowledgePresenter
import com.game.kotlin.sample.ui.activity.ContentActivity
import com.game.kotlin.sample.utils.NetWorkUtil
import com.game.kotlin.sample.widget.SpaceItemDecoration

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/5 20:19
 */
class KnowledgeFragment : BaseMvpListFragment<KnowledgeContract.View,KnowledgeContract.Presenter>(),
    KnowledgeContract.View{
    companion object {
        fun getInstance(cid: Int): KnowledgeFragment {
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * cid
     */
    private var cid: Int = 0

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * Knowledge Adapter
     */
    private val mAdapter: KnowledgeAdapter by lazy {
        KnowledgeAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun createPresenter(): KnowledgeContract.Presenter = KnowledgePresenter()

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = binding.multipleStatusView
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
        binding.swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        binding.recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

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
        mPresenter?.requestKnowledgeList(0, cid)
    }

    override fun onRefreshList() {
        mPresenter?.requestKnowledgeList(0, cid)
    }

    override fun onLoadMoreList() {
        mPresenter?.requestKnowledgeList(pageNum, cid)
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

    override fun setKnowledgeList(articles: ArticleResponseBody) {
        mAdapter.setNewOrAddData(pageNum == 0, articles.datas)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun scrollToTop() {
        binding.recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
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
                    /*Intent(activity, LoginActivity::class.java).run {
                        startActivity(this)
                    }*/
                    showToast(resources.getString(R.string.login_tint))
                }
            }
        }
    }
}