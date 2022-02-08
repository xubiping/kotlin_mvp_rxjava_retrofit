package com.game.kotlin.sample.ui.activity

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.ShareAdapter
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseMvpSwipeBackActivity
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.event.RefreshShareEvent
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showSnackMsg
import com.game.kotlin.sample.mvp.contract.ShareContract
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.ShareResponseBody
import com.game.kotlin.sample.mvp.presenter.SharePresenter
import com.game.kotlin.sample.utils.DialogUtil
import com.game.kotlin.sample.utils.NetWorkUtil
import com.game.kotlin.sample.widget.SpaceItemDecoration
import com.game.kotlin.sample.widget.SwipeItemLayout
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 22:07
 */
class ShareActivity : BaseMvpSwipeBackActivity<ShareContract.View, SharePresenter>(), ShareContract.View {

    private var pageSize = 20

    private var pageNum = 1

    private val shareAdapter: ShareAdapter by lazy {
        ShareAdapter()
    }

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

    override fun createPresenter(): SharePresenter = SharePresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_share

    override fun useEventBus(): Boolean = true

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        mLayoutStatusView = multiple_status_view
        toolbar.run {
            title = getString(R.string.my_share)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ShareActivity)
            adapter = shareAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            addItemDecoration(SpaceItemDecoration(this@ShareActivity))
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(this@ShareActivity))
        }
        shareAdapter.run {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener {
                pageNum++
                swipeRefreshLayout.isRefreshing = false
                mPresenter?.getShareList(pageNum)
            }
        }
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getShareList(1)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshShare(event: RefreshShareEvent) {
        if (event.isRefresh) {
            start()
        }
    }

    override fun showShareList(body: ShareResponseBody) {
        shareAdapter.setNewOrAddData(pageNum == 1, body.shareArticles.datas)
        if (shareAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showDeleteArticle(success: Boolean) {
        if (success) {
            showMsg(getString(R.string.share_article_delete))
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showMsg(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showMsg(resources.getString(R.string.cancel_collect_success))
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        pageNum = 1
        mPresenter?.getShareList(1)
    }

    /**
     * Item Child Click
     */
    private fun itemChildClick(item: Article, view: View, position: Int) {
        if (!NetWorkUtil.isNetworkAvailable(App.context)) {
            showSnackMsg(resources.getString(R.string.no_network))
            return
        }
        when (view.id) {
            R.id.rl_content -> {
                ContentActivity.start(this, item.id, item.title, item.link)
            }
            R.id.iv_like -> {
                if (isLogin) {
                    val collect = item.collect
                    item.collect = !collect
                    shareAdapter.setData(position, item)
                    if (collect) {
                        mPresenter?.cancelCollectArticle(item.id)
                    } else {
                        mPresenter?.addCollectArticle(item.id)
                    }
                } else {
                    Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    showMsg(resources.getString(R.string.login_tint))
                }
            }
            R.id.btn_delete -> {
                DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_delete)) { _, _ ->
                    mPresenter?.deleteShareArticle(item.id)
                    shareAdapter.removeAt(position)
                }.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                Intent(this@ShareActivity, CommonActivity::class.java).run {
                    putExtra(Constant.TYPE_KEY, Constant.Type.SHARE_ARTICLE_TYPE_KEY)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}