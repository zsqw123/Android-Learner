package com.zsqw123.learner.other.results

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/12 19:19
 */
class Obs<V>(private var value: V) {
    private val obsList = arrayListOf<(V, V) -> Unit>()
    fun addObs(callback: (old: V, new: V) -> Unit) = obsList.add(callback)
    fun removeObsAt(index: Int) = obsList.removeAt(index)
    fun clearObs() = obsList.clear()
    fun get() = value
    fun set(v: V) {
        val old = value
        value = v
        obsList.forEach { it(old, value) }
    }
}
