package com.game.kotlin.sample.mvp.presenter

import com.game.kotlin.sample.base.BasePresenter
import com.game.kotlin.sample.mvp.contract.SystemContract
import com.game.kotlin.sample.mvp.model.SystemModel

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 17:41
 */
class SystemPresenter : BasePresenter<SystemModel, SystemContract.View>(),
        SystemContract.Presenter {

    override fun createModel(): SystemModel? = SystemModel()

}