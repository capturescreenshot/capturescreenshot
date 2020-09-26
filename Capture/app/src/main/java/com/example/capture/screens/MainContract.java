package com.example.capture.screens;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface MainContract {

    interface View {
        void bindViews();
        void initialize();
        void displayAd(Drawable imageDrawable, Drawable appIcon,String bodyText,String headline);
    }

    interface Presenter {
        void startService(Context context);
        void loadAd(Context context);
        void hideApp(Context context);

    }

    interface Interactor {
        void startScreenshotService(Context context);
        //void initializeMobileAds(Context context);
        void loadNativeAd(Context context,OnAdListener onAdListener);
        void hideAppIcon(Context context);
    }

    interface OnAdListener{
        void onSuccess(Drawable imageDrawable, Drawable appIcon,String bodyText,String headline);
        void onFailed();
    }
}
