# kotlin_mvp_rxjava_retrofit
感谢 wanandroid 平台开源
项目参考：https://github.com/iceCola7/WanAndroid
通过项目，自己重新写了一遍学习。
项目主要特点：
1、View层的基类封装
全部kotlin写的，mvp架构，
BaseActivity 、BAseFragment 抽象类，封装布局文件ID初始化View 初始化数据、开始请求 状态栏等

BaseMvpActivity 、 BaseMvpFragment 分别继承 BaseActivity 和 BaseFragment 并实现了 IView 接口，将 MVP 基础架构封装起来；注：如果想使用 MVP 架构就继承 BaseMvpActivity 或者 BaseMvpFragment ，如果不适用 MVP 架构就继承 BaseActivity 或者 BaseFragment ；
BaseMvpTitleActivity 继承 BaseMvpActivity ，简单了封装了 Toolbar，可扩展

2、ext相关的封装（主要用到 kotlin扩展函数）
封装 loge 、 showToast 、 showSnackMsg 、ss 、 sss 等通用方法，项目中可以直接调用；
ss 、 sss ，这两个方法主要是对网络请求的统一封装，使用起来非常方便（亮点）；

3、网络通讯通用类
封装 RetrofitFactory 来构建不同 baseUrl 的 RetrofitService （注：项目中 baseUrl 很多的情况下，不建议使用，建议重新封装） ；
封装 cookie 相关、统一的异常处理、 CacheInterceptor 、 HeaderInterceptor 、 SaveCookieInterceptor 等；
封装 请求重连 操作，详情请见类 RetryWithDelay 。

4、MVP 基础架构
 MVP 即是 Model , View , Presenter 三层，把 Activity 中的 UI 逻辑抽象成 View 接口，把业务逻辑抽象成 Presenter 接口， Model 类还是原来的 Model ，实现了 Model 层和 View 层完全解耦。
 
5、rx 相关
  封装 SchedulerUtils 工具类、 IoMainScheduler 等；
  BaseObserver 和 BaseSubscriber 对 ResourceObserver 和 ResourceSubscriber 通用封装。
  
6、自定义控件
  封装 Toast 、 LoadingView 和 OnNoDoubleClickListener （防止连续点击）。