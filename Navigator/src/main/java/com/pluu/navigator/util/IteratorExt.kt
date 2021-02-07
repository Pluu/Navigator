package com.pluu.navigator.util

import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat

internal fun <K, V> ArrayMap<K, V>.toIteratorWithRemove(): MutableIterator<Pair<K, V>> {
    val nodes = this
    return object : MutableIterator<Pair<K, V>> {
        private var mIndex = -1
        private var mWentToNext = false

        override fun hasNext(): Boolean {
            return mIndex + 1 < nodes.size
        }

        override fun next(): Pair<K, V> {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            mWentToNext = true
            return nodes.keyAt(++mIndex).let { key ->
                key to nodes[key]!!
            }
        }

        override fun remove() {
            check(mWentToNext) { "You must call next() before you can remove an element" }
            nodes.removeAt(mIndex)
            mIndex--
            mWentToNext = false
        }
    }
}

internal fun <E> SparseArrayCompat<E>.toIteratorWithRemove(): MutableIterator<E> {
    val nodes = this
    return object : MutableIterator<E> {
        private var mIndex = -1
        private var mWentToNext = false

        override fun hasNext(): Boolean {
            return mIndex + 1 < nodes.size()
        }

        override fun next(): E {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            mWentToNext = true
            return nodes.valueAt(++mIndex)
        }

        override fun remove() {
            check(mWentToNext) { "You must call next() before you can remove an element" }
            nodes.removeAt(mIndex)
            mIndex--
            mWentToNext = false
        }
    }
}
