package com.game.kotlin.sample.rx

import com.game.kotlin.sample.rx.scheduler.IoMainScheduler

/**
 * @description:
 * @author:  xubp
 * @date :   2022/1/23 11:03
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

}