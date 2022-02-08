package com.game.kotlin.sample.ui.fragment

import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.base.BaseFragment

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:18
 */
class QrCodeFragment : BaseFragment() {

    companion object {
        fun getInstance(): QrCodeFragment = QrCodeFragment()
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_qr_code

    override fun initView(view: View) {
    }

    override fun lazyLoad() {
    }
}