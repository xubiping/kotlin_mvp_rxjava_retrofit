package com.game.kotlin.sample.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.base.BaseMvpFragment
import com.game.kotlin.sample.event.RefreshShareEvent
import com.game.kotlin.sample.mvp.contract.ShareArticleContract
import com.game.kotlin.sample.mvp.presenter.ShareArticlePresenter
import com.game.kotlin.sample.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_share_article.*
import org.greenrobot.eventbus.EventBus

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:21
 */
class ShareArticleFragment : BaseMvpFragment<ShareArticleContract.View, ShareArticlePresenter>(), ShareArticleContract.View {

    companion object {
        fun getInstance(): ShareArticleFragment = ShareArticleFragment()
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(requireActivity(), getString(R.string.submit_ing))
    }

    override fun getArticleTitle(): String = et_article_title.text.toString().trim()

    override fun getArticleLink(): String = et_article_link.text.toString().trim()

    override fun createPresenter(): ShareArticlePresenter = ShareArticlePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_share_article

    override fun showLoading() {
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun initView(view: View) {
        // 在fragment中使用 onCreateOptionsMenu 时需要在 onCrateView 中添加此方法，否则不会调用
        setHasOptionsMenu(true)
        super.initView(view)
    }

    override fun lazyLoad() {
    }

    override fun showShareArticle(success: Boolean) {
        if (success) {
            showDefaultMsg(getString(R.string.share_success))
            EventBus.getDefault().post(RefreshShareEvent(true))
            activity?.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share_article, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_article -> {
                mPresenter?.shareArticle()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}