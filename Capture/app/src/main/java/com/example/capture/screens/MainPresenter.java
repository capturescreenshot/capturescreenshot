package com.example.capture.screens;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.Interactor interactor;
    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        interactor = new MainInteractor();
    }

    @Override
    public void startService(Context context) {
        interactor.startScreenshotService(context);
    }

    @Override
    public void loadAd(Context context) {
        interactor.loadNativeAd(context,new MainContract.OnAdListener() {
            @Override
            public void onSuccess(Drawable imageDrawable, Drawable appIcon, String bodyText, String headline) {
                view.displayAd(imageDrawable,appIcon,bodyText,headline);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void hideApp(Context context) {
        interactor.hideAppIcon(context);
    }
}
