package com.game.kotlin.sample.ui.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.SearchHistoryAdapter
import com.game.kotlin.sample.base.BaseMvpSwipeBackActivity
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.mvp.contract.SearchContract
import com.game.kotlin.sample.mvp.model.bean.HotSearchBean
import com.game.kotlin.sample.mvp.model.bean.SearchHistoryBean
import com.game.kotlin.sample.mvp.presenter.SearchPresenter
import com.game.kotlin.sample.utils.CommonUtil
import com.game.kotlin.sample.utils.DisplayManager
import com.game.kotlin.sample.widget.RecyclerViewItemDecoration
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/7 22:05
 */
class SearchActivity : BaseMvpSwipeBackActivity<SearchContract.View, SearchContract.Presenter>(), SearchContract.View {

    override fun createPresenter(): SearchContract.Presenter = SearchPresenter()

    /**
     * 热搜数据
     */
    private var mHotSearchDatas = mutableListOf<HotSearchBean>()

    /**
     * SearchHistoryAdapter
     */
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter()
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        RecyclerViewItemDecoration(this)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_search

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        hot_search_flow_layout.run {
            setOnTagClickListener { view, position, parent ->
                if (mHotSearchDatas.size > 0) {
                    val hotSearchBean = mHotSearchDatas[position]
                    goToSearchList(hotSearchBean.name)
                    true
                }
                false
            }
        }

        rv_history_search.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            // addItemDecoration(recyclerViewItemDecoration)
        }

        searchHistoryAdapter.run {
            setEmptyView(R.layout.search_empty_view)
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as SearchHistoryBean
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as SearchHistoryBean
                itemChildClick(item, view, position)
            }
        }

        search_history_clear_all_tv.setOnClickListener {
            searchHistoryAdapter.setList(mutableListOf())
            mPresenter?.clearAllHistory()
        }

        mPresenter?.getHotSearchData()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.queryHistory()
    }

    override fun start() {
    }

    private fun goToSearchList(key: String) {
        mPresenter?.saveSearchKey(key)
        Intent(this, CommonActivity::class.java).run {
            putExtra(Constant.TYPE_KEY, Constant.Type.SEARCH_TYPE_KEY)
            putExtra(Constant.SEARCH_KEY, key)
            startActivity(this)
        }
    }

    override fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(historyBeans)
    }

    override fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        hot_search_flow_layout.adapter = object : TagAdapter<HotSearchBean>(hotSearchDatas) {
            override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: HotSearchBean?): View {
                val tv: TextView = LayoutInflater.from(parent?.context).inflate(
                        R.layout.flow_layout_tv,
                        hot_search_flow_layout, false
                ) as TextView
                val padding: Int = DisplayManager.dip2px(10F)
                tv.setPadding(padding, padding, padding, padding)
                tv.text = hotSearchBean?.name
                tv.setTextColor(CommonUtil.randomColor())
                return tv
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = getString(R.string.search_tint)
        searchView.setOnQueryTextListener(queryTextListener)
        searchView.isSubmitButtonEnabled = true
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_search_white_24dp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * OnQueryTextListener
     */
    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            goToSearchList(query.toString())
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    /**
     * Item Click
     */
    private fun itemClick(item: SearchHistoryBean) {
        goToSearchList(item.key)
    }

    /**
     * Item Child Click
     * @param item Article
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: SearchHistoryBean, view: View, position: Int) {
        when (view.id) {
            R.id.iv_clear -> {
                mPresenter?.deleteById(item.id)
                searchHistoryAdapter.removeAt(position)
            }
        }
    }
}