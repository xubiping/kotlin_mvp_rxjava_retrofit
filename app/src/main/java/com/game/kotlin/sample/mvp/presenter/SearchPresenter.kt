package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.SearchContract
import com.game.kotlin.sample.mvp.model.SearchModel
import com.game.kotlin.sample.mvp.model.bean.SearchHistoryBean
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:37
 */
class SearchPresenter : BasePresenter<SearchContract.Model, SearchContract.View>(), SearchContract.Presenter {


    override fun createModel(): SearchContract.Model? = SearchModel()

    override fun deleteById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }

    }

    override fun clearAllHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
            uiThread {

            }
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val beans = LitePal.where("key = '${key.trim()}'").find(SearchHistoryBean::class.java)
            if (beans.size == 0) {
                historyBean.save()
            } else {
                deleteById(beans[0].id)
                historyBean.save()
            }
        }
    }

    override fun queryHistory() {
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            historyBeans.reverse()
            uiThread {
                mView?.showHistoryData(historyBeans)
            }
        }
    }

    override fun getHotSearchData() {
        mModel?.getHotSearchData()?.ss(mModel, mView) {
            mView?.showHotSearchData(it.data)
        }
    }

}