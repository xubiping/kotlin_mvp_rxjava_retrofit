package com.game.kotlin.sample.http.interceptor

import com.game.kotlin.sample.constant.HttpConstant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:27
 * @desc SaveCookieInterceptor: 保存 Cookie
 */
class SaveCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val domain = request.url().host()
        // set-cookie maybe has multi, login to save cookie
        if ((requestUrl.contains(HttpConstant.SAVE_USER_LOGIN_KEY)
                        || requestUrl.contains(HttpConstant.SAVE_USER_REGISTER_KEY))
                && !response.headers(HttpConstant.SET_COOKIE_KEY).isEmpty()) {
            val cookies = response.headers(HttpConstant.SET_COOKIE_KEY)
            val cookie = HttpConstant.encodeCookie(cookies)
            HttpConstant.saveCookie(requestUrl, domain, cookie)
        }
        return response
    }
}