package com.game.kotlin.sample.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentTransaction
import com.game.kotlin.sample.R
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseActivity
import com.game.kotlin.sample.base.BaseMvpActivity
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.databinding.ActivityMainBinding
import com.game.kotlin.sample.databinding.ToolbarBinding
import com.game.kotlin.sample.event.ColorEvent
import com.game.kotlin.sample.event.LoginEvent
import com.game.kotlin.sample.event.RefreshHomeEvent
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.contract.MainContract
import com.game.kotlin.sample.mvp.model.bean.UserInfoBody
import com.game.kotlin.sample.mvp.presenter.MainPresenter
import com.game.kotlin.sample.ui.fragment.HomeFragment
import com.game.kotlin.sample.ui.fragment.WeChatFragment
import com.game.kotlin.sample.utils.DialogUtil
import com.game.kotlin.sample.utils.Preference
import com.game.kotlin.sample.utils.SettingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/29 16:22
 */
class MainActivity : BaseMvpActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {
    /* lateinit var binding:ActivityMainBinding
     lateinit var toolbarBinding: ToolbarBinding*/
    private val BOTTOM_INDEX: String = "bottom_index"

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_SQUARE = 0x02
    private val FRAGMENT_WECHAT = 0x03
    private val FRAGMENT_SYSTEM = 0x04
    private val FRAGMENT_PROJECT = 0x05

    private var mIndex = FRAGMENT_HOME
    private var mHomeFragment: HomeFragment? = null
    private var mWeChatFragment: WeChatFragment? = null

    private var mExitTime: Long = 0

    /**
     * local username
     */
    private var username: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * username TextView
     */
    private var nav_username: TextView? = null

    /**
     * user_id TextView
     */
    private var nav_user_id: TextView? = null

    /**
     * user_grade TextView
     */
    private var nav_user_grade: TextView? = null

    /**
     * user_rank TextView
     */
    private var nav_user_rank: TextView? = null

    /**
     * score TextView
     */
    private var nav_score: TextView? = null

    /**
     * rank ImageView
     */
    private var nav_rank: ImageView? = null

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    override fun useEventBus(): Boolean = true

    override fun initData() {
        //Beta.checkAppUpgrade(false,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null){
            mIndex = savedInstanceState?.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        /*binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarBinding.toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        binding.bottomNavigation.run {*/
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        initDrawerLayout()

        initNavView()

        showFragment(mIndex)

        /* binding.floatingActionBtn.run {
            // setOnClickListener(onFABClickListener)
         }*/

    }

    override fun start() {
        // 获取用户个人信息
        mPresenter?.getUserInfo()
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    /**
     * init NavigationView
     */
    private fun initNavView(){
        // binding.navView.run {
        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            nav_user_id = getHeaderView(0).findViewById(R.id.tv_user_id)
            nav_user_grade = getHeaderView(0).findViewById(R.id.tv_user_grade)
            nav_user_rank = getHeaderView(0).findViewById(R.id.tv_user_rank)
            nav_rank = getHeaderView(0).findViewById(R.id.iv_rank)
            nav_score = MenuItemCompat.getActionView(nav_view.menu.findItem(R.id.nav_score))as TextView
            nav_score?.gravity = Gravity.CENTER_VERTICAL
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username?.run {
            text = if (!isLogin) getString(R.string.go_login) else username
            setOnClickListener {
                if (!isLogin) {
                    /* Intent(this@MainActivity, LoginActivity::class.java).run {
                         startActivity(this)
                     }*/
                }
            }
        }
        nav_rank?.setOnClickListener{
            //startActivity(Intent(this@MainActivity, RankActivity::class.java))
        }
    }

    private fun initDrawerLayout(){
        /*binding.run {
            val toggle = ActionBarDrawerToggle(this@MainActivity,binding.drawerLayout,toolbarBinding.toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }*/
    }

    override fun showUserInfo(bean: UserInfoBody) {
        App.userInfo = bean

        nav_user_id?.text = bean.userId.toString()
        nav_user_grade?.text = (bean.coinCount / 100 + 1).toString()
        nav_user_rank?.text = bean.rank.toString()
        nav_score?.text = bean.coinCount.toString()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent){
        if (event.isLogin) {
            nav_username?.text = username
            nav_view.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoad()
            mPresenter?.getUserInfo()
        } else {
            nav_username?.text = resources.getString(R.string.go_login)
            nav_view.menu.findItem(R.id.nav_logout).isVisible = false
            mHomeFragment?.lazyLoad()
            // 重置用户信息
            nav_user_id?.text = getString(R.string.nav_line_4)
            nav_user_grade?.text = getString(R.string.nav_line_2)
            nav_user_rank?.text = getString(R.string.nav_line_2)
            nav_score?.text = ""
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event: RefreshHomeEvent) {
        if (event.isRefresh) {
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_INDEX, mIndex)
    }

    /**
     * 展示Fragment
     * @param index
     */
    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index
        when (index){
            FRAGMENT_HOME  // 首页
            ->{
                toolbar.title = getString(R.string.app_name)
                if(mHomeFragment == null){
                    mHomeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.container,mHomeFragment!!)
                } else {
                    transaction.show(mHomeFragment!!)
                }

            }
            FRAGMENT_WECHAT // 公众号
            -> {
                toolbar.title = getString(R.string.wechat)
                if (mWeChatFragment == null) {
                    mWeChatFragment = WeChatFragment.getInstance()
                    transaction.add(R.id.container, mWeChatFragment!!, "wechat")
                } else {
                    transaction.show(mWeChatFragment!!)
                }
            }
        }
        transaction.commit()
    }
    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction:FragmentTransaction){
        mHomeFragment?.let { transaction.hide(it) }
        /* mSquareFragment?.let { transaction.hide(it) }
         mSystemFragment?.let { transaction.hide(it) }
         mProjectFragment?.let { transaction.hide(it) }*/
        mWeChatFragment?.let { transaction.hide(it) }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.action_home -> {
                        showFragment(FRAGMENT_HOME)
                        true
                    }
                    /*R.id.action_square -> {
                        showFragment(FRAGMENT_SQUARE)
                        true
                    }
                    R.id.action_system -> {
                        showFragment(FRAGMENT_SYSTEM)
                        true
                    }
                    R.id.action_project -> {
                        showFragment(FRAGMENT_PROJECT)
                        true
                    }*/
                    R.id.action_wechat -> {
                        showFragment(FRAGMENT_WECHAT)
                        true
                    }
                    else -> {
                        false
                    }

                }
            }
    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    /*R.id.nav_score -> {
                        if (isLogin) {
                            Intent(this@MainActivity, ScoreActivity::class.java).run {
                                startActivity(this)
                            }
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            goLogin()
                        }
                    }
                    R.id.nav_collect -> {
                        if (isLogin) {
                            goCommonActivity(Constant.Type.COLLECT_TYPE_KEY)
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            goLogin()
                        }
                    }
                    R.id.nav_share -> {
                        if (isLogin) {
                            startActivity(Intent(this, ShareActivity::class.java))
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            goLogin()
                        }
                    }
                    R.id.nav_setting -> {
                        Intent(this@MainActivity, SettingActivity::class.java).run {
                            // putExtra(Constant.TYPE_KEY, Constant.Type.SETTING_TYPE_KEY)
                            startActivity(this)
                        }
                    }
                    //R.id.nav_about_us -> {
                    //    goCommonActivity(Constant.Type.ABOUT_US_TYPE_KEY)
                    //}
                    R.id.nav_logout -> {
                        logout()
                    }
                    R.id.nav_night_mode -> {
                        if (SettingUtil.getIsNightMode()) {
                            SettingUtil.setIsNightMode(false)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        } else {
                            SettingUtil.setIsNightMode(true)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                        recreate()
                    }
                    R.id.nav_todo -> {
                        if (isLogin) {
                            Intent(this@MainActivity, TodoActivity::class.java).run {
                                startActivity(this)
                            }
                        } else {
                            showToast(resources.getString(R.string.login_tint))
                            goLogin()
                        }
                    }*/
                }
                // drawer_layout.closeDrawer(GravityCompat.START)
                true
            }
    private fun goCommonActivity(type:String){
        /* Intent(this@MainActivity,CommonA)*/
    }
    /**
     * 去登陆页面
     */
    private fun goLogin() {
        /*Intent(this@MainActivity, LoginActivity::class.java).run {
            startActivity(this)
        }*/
    }

    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (mHomeFragment != null) {
                fragmentTransaction.remove(mHomeFragment!!)
            }
            /*if (mSquareFragment != null) {
                fragmentTransaction.remove(mSquareFragment!!)
            }
            if (mSystemFragment != null) {
                fragmentTransaction.remove(mSystemFragment!!)
            }
            if (mProjectFragment != null) {
                fragmentTransaction.remove(mProjectFragment!!)
            }*/
            if (mWeChatFragment != null) {
                fragmentTransaction.remove(mWeChatFragment!!)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }
    /**
     * 退出登录 Dialog
     */
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this,resources.getString(R.string.logout_ing))
    }

    /**
     * Logout
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_logout),
                DialogInterface.OnClickListener { _, _ ->
                    mDialog.show()
                    mPresenter?.logout()
                }).show()
    }

    override fun showLogoutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                // CookieManager().clearAllCookies()
                Preference.clearPreference()
                uiThread {
                    mDialog.dismiss()
                    showToast(resources.getString(R.string.logout_success))
                    username = nav_username?.text.toString().trim()
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                }
            }
        }
    }
    /**
     * FAB 监听
     */
    private val onFABClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                mHomeFragment?.scrollToTop()
            }
            /* FRAGMENT_SQUARE -> {
                 mSquareFragment?.scrollToTop()
             }
             FRAGMENT_SYSTEM -> {
                 mSystemFragment?.scrollToTop()
             }
             FRAGMENT_PROJECT -> {
                 mProjectFragment?.scrollToTop()
             }*/
            FRAGMENT_WECHAT -> {
                mWeChatFragment?.scrollToTop()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mIndex != FRAGMENT_SQUARE) {
            // menuInflater.inflate(R.menu.menu_activity_main, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /* when (item.itemId) {
             R.id.action_search -> {
                 Intent(this, SearchActivity::class.java).run {
                     startActivity(this)
                 }
                 return true
             }
         }*/
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeFragment = null
        /* mSquareFragment = null
         mSystemFragment = null
         mProjectFragment = null*/
        mWeChatFragment = null
    }

}