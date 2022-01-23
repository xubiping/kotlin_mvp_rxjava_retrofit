package com.game.kotlin.sample.mvp.contract

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:19
 */
interface ContentContract {

    interface View : CommonContract.View {

    }

    interface Presenter : CommonContract.Presenter<View> {

    }

    interface Model : CommonContract.Model {

    }

}