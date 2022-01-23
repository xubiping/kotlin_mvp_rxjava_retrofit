package com.game.kotlin.sample.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ninering.rocen.an.R

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 10:43
 */
class CustomToast {

    private var toast: Toast? = null
    private var textView: TextView? = null

    constructor(context: Context?, message: String) : this(context, message, Toast.LENGTH_SHORT)

    constructor(context: Context?, message: String, duration: Int) {
        toast = Toast(context)
        toast?.duration = duration
        val view = View.inflate(context, R.layout.toast_custom, null)
        textView = view.findViewById(R.id.tv_prompt)
        textView?.text = message
        toast?.view = view
        toast?.setGravity(Gravity.CENTER, 0, 0)
    }

    fun show() {
        toast?.show()
    }
}