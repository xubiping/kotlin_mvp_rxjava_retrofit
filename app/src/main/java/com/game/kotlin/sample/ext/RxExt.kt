package com.game.kotlin.sample.ext

import com.game.kotlin.sample.R
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.IModel
import com.game.kotlin.sample.base.IView
import com.game.kotlin.sample.http.exception.ErrorStatus
import com.game.kotlin.sample.http.exception.ExceptionHandle
import com.game.kotlin.sample.http.function.RetryWithDelay
import com.game.kotlin.sample.mvp.model.bean.BaseBean
import com.game.kotlin.sample.rx.SchedulerUtils
import com.game.kotlin.sample.utils.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 15:12
 */
fun <T : BaseBean> Observable<T>.ss(
        model: IModel?,
        view: IView?,
        isShowLoading: Boolean = true,
        onSuccess: (T) -> Unit
) {
    this.compose(SchedulerUtils.ioToMain())
            .retryWhen(RetryWithDelay())
            .subscribe(object : Observer<T> {
                override fun onComplete() {
                    view?.hideLoading()
                }

                override fun onSubscribe(d: Disposable) {
                    if (isShowLoading) view?.showLoading()
                    model?.addDisposable(d)
                    if (!NetWorkUtil.isNetworkConnected(App.instance)) {
                        view?.showDefaultMsg(App.instance.resources.getString(R.string.network_unavailable_tip))
                        onComplete()
                    }
                }

                override fun onNext(t: T) {
                    when {
                        t.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(t)
                        t.errorCode == ErrorStatus.TOKEN_INVALID -> {
                            // Token 过期，重新登录
                        }
                        else -> view?.showDefaultMsg(t.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    view?.hideLoading()
                    view?.showError(ExceptionHandle.handleException(t))
                }
            })
}

fun <T : BaseBean> Observable<T>.sss(
        view: IView?,
        isShowLoading: Boolean = true,
        onSuccess: (T) -> Unit,
        onError: ((T) -> Unit)? = null
): Disposable {
    if (isShowLoading) view?.showLoading()
    return this.compose(SchedulerUtils.ioToMain())
            .retryWhen(RetryWithDelay())
            .subscribe({
                when {
                    it.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(it)
                    it.errorCode == ErrorStatus.TOKEN_INVALID -> {
                        // Token 过期，重新登录
                    }
                    else -> {
                        if (onError != null) {
                            onError.invoke(it)
                        } else {
                            if (it.errorMsg.isNotEmpty())
                                view?.showDefaultMsg(it.errorMsg)
                        }
                    }
                }
                view?.hideLoading()
            }, {
                view?.hideLoading()
                view?.showError(ExceptionHandle.handleException(it))
            })
}