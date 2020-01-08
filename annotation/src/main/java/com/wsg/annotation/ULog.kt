package com.wsg.annotation


/**
 *  @author WuSG
 *  @date : 2020-01-06 17:03
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class ULog(val tagName: Array<String> = [])
