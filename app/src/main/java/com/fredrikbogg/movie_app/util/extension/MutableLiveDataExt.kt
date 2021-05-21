package com.fredrikbogg.movie_app.util.extension

import androidx.lifecycle.MutableLiveData

fun <T, X : List<T>> MutableLiveData<MutableList<T>>.appendList(list: X) {
    val newList = this.value ?: mutableListOf()
    newList.addAll(list)
    this.value = newList
}

fun <T> MutableLiveData<MutableList<T>>.clear() {
    val newList = this.value ?: mutableListOf()
    newList.clear()
    this.value = newList
}

