package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.mvp.contract.ContentContract
import com.game.kotlin.sample.mvp.model.ContentModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:24
 */
class ContentPresenter : CommonPresenter<ContentContract.Model, ContentContract.View>(), ContentContract.Presenter {

    override fun createModel(): ContentContract.Model? = ContentModel()

}