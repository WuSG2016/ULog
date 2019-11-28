package com.wsg.common

object LoggerPrinter {

    private const val MIN_STACK_OFFSET = 3
    /**
     * It is used for json pretty print
     */
    const val JSON_INDENT = 2

    fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LoggerPrinter::class.java.name && name != Logger::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }
}