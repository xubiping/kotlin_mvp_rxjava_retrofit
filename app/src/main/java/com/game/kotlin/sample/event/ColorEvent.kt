package com.game.kotlin.sample.event

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:29
 */
class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor())