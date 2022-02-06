package com.game.kotlin.sample.ui.activity

import android.content.Intent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.game.kotlin.sample.R
import com.game.kotlin.sample.base.BaseActivity
import com.game.kotlin.sample.databinding.ActivitySplashBinding

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/29 11:53
 */
class SplashActivity : BaseActivity() {

    lateinit var alphaAnimation: AlphaAnimation
    lateinit var binding: ActivitySplashBinding;
    override fun attachLayoutRes(): Int = R.layout.activity_splash
    override fun initData() {
    }

    override fun initView() {
        //setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        var rootView:View = binding.root
        setContentView(rootView)
        alphaAnimation = AlphaAnimation(0.3F,1.0F)
        alphaAnimation?.run {
            duration = 2000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    jumpToMain()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        binding.layoutSplash.startAnimation(alphaAnimation)
    }

    override fun start() {
    }
    fun jumpToMain(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}