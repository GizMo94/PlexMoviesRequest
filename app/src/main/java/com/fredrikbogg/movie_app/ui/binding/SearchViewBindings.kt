package com.fredrikbogg.movie_app.ui.binding

import android.widget.SearchView
import androidx.databinding.BindingAdapter


@BindingAdapter("search_listener")
fun SearchView.setOnQueryTextListener(listener : SearchView.OnQueryTextListener) {
    this.setOnQueryTextListener(listener)
}
@BindingAdapter("clear_listener")
fun SearchView.setOnQueryClearListener(listener : SearchView.OnCloseListener) {
    this.setOnCloseListener(listener)
}










