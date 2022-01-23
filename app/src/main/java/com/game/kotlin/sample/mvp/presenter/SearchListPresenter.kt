package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.ext.ss
import com.game.kotlin.sample.mvp.contract.SearchListContract
import com.game.kotlin.sample.mvp.model.SearchListModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:36
 */
class SearchListPresenter : CommonPresenter<SearchListContract.Model, SearchListContract.View>(), SearchListContract.Presenter {

    override fun createModel(): SearchListContract.Model? = SearchListModel()

    override fun queryBySearchKey(page: Int, key: String) {
        mModel?.queryBySearchKey(page, key)?.ss(mModel, mView, page == 0) {
            mView?.showArticles(it.data)
        }
    }

}