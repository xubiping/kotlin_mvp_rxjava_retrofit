package com.game.kotlin.sample.http.exception

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:08
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}