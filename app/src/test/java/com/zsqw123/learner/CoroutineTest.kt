package com.zsqw123.learner

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/10/1 19:16
 */
fun main(): Unit = runBlocking {
    launch {
        delay(1)
        println(1)
    }
    launch { println(2) }
}

suspend fun work0(): Int {
    delay(200);return 0
}

suspend fun work1(input: Int): Int {
    delay(200);return input + 1
}

suspend fun work2(input: Int): Int {
    delay(200);return input + 2
}

suspend fun coroutineTestFun() {
    coroutineScope {
    }
    val work0 = work0()
    println(work0)
    val work1 = work1(work0)
    println(work1)
    val work2 = work2(work1)
    println(work2)
}

//var a = 1
//var nowThread: Thread? = null
//var threadSwitchTime = 0
//fun realAdd() = GlobalScope.async(Dispatchers.IO) {
//    val cur = Thread.currentThread()
//    if (nowThread != cur) {
//        threadSwitchTime++
//        nowThread = cur
//    }
//    a++
//}
//
//suspend fun addTest() {
//    println(measureTimeMillis {
//        repeat(1000_000) { realAdd() }
//        delay(5000)
//        println(a)
//        println(threadSwitchTime)
//    })
//}