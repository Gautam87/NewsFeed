package com.gautam.newsapp.base;

public interface BaseView<T> {
    void setPresenter(T presenter);

    /**
     * Show progress bar dialog
     */
    void showComplete();
}
