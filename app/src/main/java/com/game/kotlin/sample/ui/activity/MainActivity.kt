package com.game.kotlin.sample.ui.activity

import com.game.kotlin.sample.base.BaseActivity
import com.game.kotlin.sample.databinding.ActivityMainBinding

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/29 16:22
 */
class MainActivity : BaseActivity() {
    lateinit var binding:ActivityMainBinding
    override fun initData() {

    }

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun start() {

    }

}