package com.game.kotlin.sample.webclient

import android.webkit.WebViewClient

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 10:24
 */
object WebClientFactory {

    val JIAN_SHU = "https://www.jianshu.com"

    fun create(url: String): WebViewClient {
        return when {
            url.startsWith(JIAN_SHU) -> JianShuWebClient()
            else -> BaseWebClient()
        }
    }

}