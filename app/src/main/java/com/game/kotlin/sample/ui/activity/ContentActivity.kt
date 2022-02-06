package com.game.kotlin.sample.ui.activity

import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.game.kotlin.sample.R
import com.game.kotlin.sample.base.BaseMvpSwipeBackActivity
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.databinding.ActivityContentBinding
import com.game.kotlin.sample.databinding.ToolbarBinding
import com.game.kotlin.sample.event.RefreshHomeEvent
import com.game.kotlin.sample.ext.getAgentWeb
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.ContentContract
import com.game.kotlin.sample.mvp.presenter.ContentPresenter
import com.game.kotlin.sample.webclient.WebClientFactory
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import org.greenrobot.eventbus.EventBus

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/5 15:04
 */
class ContentActivity : BaseMvpSwipeBackActivity<ContentContract.View,ContentContract.Presenter>(),ContentContract.View{

    lateinit var toolbarBinding:ToolbarBinding
    lateinit var activityContentBinding:ActivityContentBinding
    private var mAgentWeb: AgentWeb? = null

    private var shareTitle:String = ""
    private var shareUrl:String = ""
    private var shareId:Int = -1

    companion object{
        fun start(content:Context?,id:Int,title:String,url:String,bundle:Bundle?=null){
            Intent(content,ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_ID_KEY,id)
                putExtra(Constant.CONTENT_TITLE_KEY,title)
                putExtra(Constant.CONTENT_URL_KEY,url)
                content?.startActivity(this,bundle)
            }
        }
        fun start(content: Context?,url: String){
            start(content,-1,"",url)
        }
    }

    override fun createPresenter(): ContentContract.Presenter = ContentPresenter()

    override fun initData() {

    }

    override fun initView() {
        super.initView()
        toolbarBinding = ToolbarBinding.inflate(layoutInflater)
        activityContentBinding =  ActivityContentBinding.inflate(layoutInflater)
        intent.extras?.let{
            shareId = it.getInt(Constant.CONTENT_TITLE_KEY,-1)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY,"")
            shareUrl = it.getString(Constant.CONTENT_URL_KEY,"")
        }
        toolbarBinding.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbarBinding.tvTitle.apply {
            text = getString(R.string.loading)
            visibility = View.VISIBLE
            postDelayed({
                toolbarBinding.tvTitle.isSelected = true
            },2000)
        }
        initWebView()
    }

    /**
     * 初始化 WebView
     */
    private fun initWebView(){
        val webView = NestedScrollAgentWebView(this)
        val layoutParams = CoordinatorLayout.LayoutParams(-1,-1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()
        mAgentWeb = shareUrl.getAgentWeb(
                this,
                activityContentBinding.clMain,
                layoutParams,
                webView,
                WebClientFactory.create(shareUrl),
                mWebChromeClient,
                mThemeColor)
        mAgentWeb?.webCreator?.webView?.apply {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    override fun start() {

    }

    private val mWebChromeClient = object : WebChromeClient(){
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            toolbarBinding.tvTitle?.text = title
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if(success){
            showToast(getString(R.string.collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if(success){
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getString(
                            R.string.share_article_url,
                            getString(R.string.app_name), shareTitle, shareUrl
                    ))
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
            R.id.action_like -> {
                if (isLogin) {
                    if (shareId == -1) return true
                    mPresenter?.addCollectArticle(shareId)
                } else {
                   /* Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }*/
                    showToast(resources.getString(R.string.login_tint))
                }
                return true
            }
            R.id.action_browser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        mAgentWeb?.let {
            if (!it.back()) {
                super.onBackPressed()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (mAgentWeb?.handleKeyEvent(keyCode, event)!!) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

}