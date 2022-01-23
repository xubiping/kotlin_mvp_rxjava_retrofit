package com.game.kotlin.sample.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc NetCacheInterceptor: 有网的时候缓存
 * @author:  xubp
 * @date :   2022/1/23 11:24
 */
class NetCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (NetWorkUtil.isNetworkAvailable(App.context)) {
            val maxAge = 60 * 3
            // 有网络时 设置缓存超时时间0时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
        }

        return response
    }
}