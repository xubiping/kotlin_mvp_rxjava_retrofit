package com.game.kotlin.sample.ui.fragment

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.bgabanner.BGABanner
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.HomeAdapter
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseMvpFragment
import com.game.kotlin.sample.databinding.FragmentRefreshLayoutBinding
import com.game.kotlin.sample.databinding.ItemHomeBannerBinding
import com.game.kotlin.sample.databinding.ItemHomeListBinding
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showSnackMsg
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.HomeContract
import com.game.kotlin.sample.mvp.model.bean.Article
import com.game.kotlin.sample.mvp.model.bean.ArticleResponseBody
import com.game.kotlin.sample.mvp.model.bean.Banner
import com.game.kotlin.sample.mvp.presenter.HomePresenter
import com.game.kotlin.sample.ui.activity.ContentActivity
import com.game.kotlin.sample.utils.ImageLoader
import com.game.kotlin.sample.utils.NetWorkUtil
import com.game.kotlin.sample.widget.SpaceItemDecoration
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.item_home_banner.*

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/3 15:39
 */
class HomeFragment : BaseMvpFragment<HomeContract.View,HomeContract.Presenter>(),HomeContract.View{
    /*lateinit var binding:FragmentRefreshLayoutBinding
    lateinit var itemHomeBannerBinding:ItemHomeBannerBinding*/

    companion object{
        fun getInstance():HomeFragment = HomeFragment()
    }

    override fun createPresenter(): HomePresenter = HomePresenter()

    /**
     * banner datas
     */
    private lateinit var bannerDatas:ArrayList<Banner>

    /**
     * banner viw
     */
    private var bannerView:View? = null

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    private val homeAdapter:HomeAdapter by lazy {
        HomeAdapter()
    }

    /**
     * Banner Adapter
     */
    private val bannerAdapter:BGABanner.Adapter<ImageView,String> by lazy {
        BGABanner.Adapter<ImageView,String>{
            bgaBanner, imageView, feedImageUrl, position ->
            ImageLoader.load(activity, feedImageUrl, imageView)
        }
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * pageNum
     */
    private var pageNum = 0

    override fun attachLayoutRes():Int = R.layout.fragment_refresh_layout
    override fun initView(view: View) {
        super.initView(view)
        /*binding = FragmentRefreshLayoutBinding.inflate(layoutInflater)
        itemHomeBannerBinding = ItemHomeBannerBinding.inflate(layoutInflater)*/
        //mLayoutStatusView = binding.root
        mLayoutStatusView = multiple_status_view
        swipeRefreshLayout.setOnRefreshListener {
            pageNum = 0
            mPresenter?.requestHomeData()
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
       // bannerView = itemHomeBannerBinding.root
        bannerView = layoutInflater.inflate(R.layout.item_home_banner, null)
        banner?.run {
            setDelegate(bannerDelegate)
        }
        homeAdapter.run {
            addHeaderView(bannerView!!)
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener {
                swipeRefreshLayout.isRefreshing = false
                pageNum++
                mPresenter?.requestArticles(pageNum)
            }
        }

    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestHomeData()
    }

    override fun scrollToTop() {
        recyclerView.run {
            if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() >20){
                scrollToPosition(0)
            }else{
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
       swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun setBanner(banners: List<Banner>) {
        bannerDatas = banners as ArrayList<Banner>
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        Observable.fromIterable(banners)
                .subscribe{
                    list ->
                    bannerFeedList.add(list.imagePath)
                    bannerTitleList.add(list.title)
                }
        banner?.run{
            setAutoPlayAble(bannerFeedList.size > 1)
            setData(bannerFeedList,bannerTitleList)
            setAdapter(bannerAdapter)
        }
    }

    override fun setArticles(articles: ArticleResponseBody) {
        homeAdapter.setNewOrAddData(pageNum == 0,articles.datas)
        if(homeAdapter.data.isEmpty()){
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if(success){
            showToast(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if(success){
            showToast(resources.getString(R.string.cancel_collect_success))
        }
    }

    private fun itemClick(item:Article){
        ContentActivity.start(activity,item.id,item.title,item.link)
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
                    homeAdapter.setData(position, item)
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

    /**
     * BannerClickListener
     */
    private val bannerDelegate = BGABanner.Delegate<ImageView,String>{
        banner, itemView, model, position ->
        if(bannerDatas.size>0){
            val data = bannerDatas[position]
            ContentActivity.start(activity,data.id,data.title,data.url)
        }
    }
}