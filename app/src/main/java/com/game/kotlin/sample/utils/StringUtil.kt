package com.game.kotlin.sample.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 9:36
 */
object StringUtil {

    fun getString(stream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(stream, "utf-8"))
        val sb = StringBuilder()
        var s: String? = reader.readLine()
        while (s != null) {
            sb.append(s).append("\n")
            s = reader.readLine()
        }
        return sb.toString()
    }

}