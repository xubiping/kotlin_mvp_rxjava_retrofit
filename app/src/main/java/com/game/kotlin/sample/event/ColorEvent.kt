package com.game.kotlin.sample.event

import com.game.kotlin.sample.utils.SettingUtil

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:29
 */
class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor())