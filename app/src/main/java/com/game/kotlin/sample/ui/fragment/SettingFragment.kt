package com.game.kotlin.sample.ui.fragment

import android.os.Bundle
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.base.BaseFragment

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:21
 */
class SettingFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle): SettingFragment {
            val fragment = SettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_setting

    override fun initView(view: View) {
    }

    override fun lazyLoad() {
    }
}