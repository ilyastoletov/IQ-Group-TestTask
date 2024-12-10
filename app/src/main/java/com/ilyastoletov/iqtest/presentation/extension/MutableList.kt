package com.ilyastoletov.iqtest.presentation.extension

fun <T> MutableList<T>.toggleItem(item: T): MutableList<T> =
    this.apply { if (item in this) remove(item) else add(item) }