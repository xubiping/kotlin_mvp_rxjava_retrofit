package com.game.kotlin.sample.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.chad.library.BuildConfig
import com.game.kotlin.sample.R
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.ext.showToast
import com.game.kotlin.sample.mvp.model.bean.UserInfoBody
import com.game.kotlin.sample.utils.CommonUtil
import com.game.kotlin.sample.utils.DisplayManager
import com.game.kotlin.sample.utils.SettingUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal
import java.util.*
import kotlin.properties.Delegates

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:56
 */
class App : Application() {

    private var refWatcher: RefWatcher? = null//监控内存

    companion object {
        val TAG = "wan_android"

        //Kotlin 标准库中所提供的最后一个内建代理是 Delegates.notNull()。notNull() 容许一个属性能够延后一段时间初始化，与 lateinit 相似。因为 notNull() 会为每一个属性建立额外的对象，因此大多数状况下推荐使用 lateinit。不过，您能够将 notNull() 与原生类型一同使用，这点是 lateinit 所不支持的
        var context: Context by Delegates.notNull()
            private set

        lateinit var instance: Application

        fun getRefWatcher(context: Context): RefWatcher? {
            val app = context.applicationContext as App
            return app.refWatcher
        }

        // 用户信息
        var userInfo: UserInfoBody? = null

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        refWatcher = setupLeakCanary()
        initConfig()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        initTheme()
        initLitePal()
        initBugly()
        // AutoDensityUtil.init()
    }

    /**
     * 初始化 Bugly
     */
    private fun initBugly() {
        if (BuildConfig.DEBUG) {
            return
        }
        /**
         * true表示app启动自动初始化升级模块; false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        //Beta.autoInit = false
        /**
         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = false
        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        //Beta.upgradeCheckPeriod = 60 * 1000
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.ic_launcher
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        /**
         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
        //Beta.canShowUpgradeActs.add(MainActivity::class.java)

        // 获取当前进程名
        val processName = CommonUtil.getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(applicationContext)
        strategy.isUploadProcess = false || processName == applicationContext.packageName

        Beta.upgradeStateListener = upgradeStateListener

        // 自定义更新布局要设置在 init 之前
        // R.layout.layout_upgrade_dialog 文件要注意两点
        // 注意1: 半透明背景要自己加上
        // 注意2: 即使自定义的弹窗不需要title, info等这些信息, 也需要将对应的tag标出出来, 一共有5个
        Beta.upgradeDialogLayoutId = R.layout.layout_upgrade_dialog
        //Beta.dialogFullScreen = true
        // CrashReport.initCrashReport(applicationContext, Constant.BUGLY_ID, BuildConfig.DEBUG, strategy)
        Bugly.init(applicationContext, Constant.BUGLY_ID, BuildConfig.DEBUG, strategy)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
        // 安装 Tinker
        Beta.installTinker()
    }

    /**
     * 初始化 LitePal
     */
    private fun initLitePal() {
        LitePal.initialize(this)
    }

    /**
     * 初始化主题
     */
    private fun initTheme() {

        if (SettingUtil.getIsAutoNightMode()) {
            val nightStartHour = SettingUtil.getNightStartHour().toInt()
            val nightStartMinute = SettingUtil.getNightStartMinute().toInt()
            val dayStartHour = SettingUtil.getDayStartHour().toInt()
            val dayStartMinute = SettingUtil.getDayStartMinute().toInt()

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val nightValue = nightStartHour * 60 + nightStartMinute
            val dayValue = dayStartHour * 60 + dayStartMinute
            val currentValue = currentHour * 60 + currentMinute

            if (currentValue >= nightValue || currentValue <= dayValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SettingUtil.setIsNightMode(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SettingUtil.setIsNightMode(false)
            }
        } else {
            // 获取当前的主题
            if (SettingUtil.getIsNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
    }

    /**
     * 初始化配置
     */
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()//设置logger 信息
                .showThreadInfo(false)  // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    private val upgradeStateListener = object : UpgradeStateListener {
        override fun onDownloadCompleted(isManual: Boolean) {
        }

        override fun onUpgradeSuccess(isManual: Boolean) {
        }

        override fun onUpgradeFailed(isManual: Boolean) {
            if (isManual) {
                showToast(getString(R.string.check_version_fail))
            }
        }

        override fun onUpgrading(isManual: Boolean) {
            if (isManual) {
                showToast(getString(R.string.check_version_ing))
            }
        }

        override fun onUpgradeNoVersion(isManual: Boolean) {
            if (isManual) {
                showToast(getString(R.string.check_no_version))
            }
        }
    }

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }


}