package com.jaky.demo.surface.ui.view;

/**
 * Created by Jack on 2017/12/31.
 */

public interface BaseView {

    void showLoading(String msg);


    void hideLoading();

    void showError(String msg);

    void showException(String msg);

    void showNetError();

}
