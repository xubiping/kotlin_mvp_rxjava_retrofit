package com.game.kotlin.sample.event

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:32
 */
class TodoEvent(var type: String, var curIndex: Int)

class TodoTypeEvent(var type: Int)