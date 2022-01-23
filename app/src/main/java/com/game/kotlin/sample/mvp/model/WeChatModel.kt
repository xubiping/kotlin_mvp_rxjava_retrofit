package com.game.kotlin.sample.mvp.model

import com.game.kotlin.sample.base.BaseModel
import com.game.kotlin.sample.http.RetrofitHelper
import com.game.kotlin.sample.mvp.contract.WeChatContract
import com.game.kotlin.sample.mvp.model.bean.HttpResult
import com.game.kotlin.sample.mvp.model.bean.WXChapterBean
import io.reactivex.Observable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:18
 */
class WeChatModel : BaseModel(), WeChatContract.Model {

    override fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>> {
        return RetrofitHelper.service.getWXChapters()
    }

}