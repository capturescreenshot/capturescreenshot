package com.example.capture.screens;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.example.capture.helperclasses.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainInteractor implements MainContract.Interactor {

    public MainInteractor() {
    }

    @Override
    public void startScreenshotService(Context context) {
        Toast.makeText(context, "Service Started", Toast.LENGTH_LONG).show();
        // Log.d(Constants.TAG,"Screenshot service interactor called");
        Util.scheduleJob(context);
    }


    public void initializeMobileAds(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    @Override
    public void loadNativeAd(Context context,final MainContract.OnAdListener onAdListener) {

        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                                        @Override
                                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                            onAdListener.onSuccess(unifiedNativeAd.getImages().get(0).getDrawable(), unifiedNativeAd.getIcon().getDrawable(), unifiedNativeAd.getBody(), unifiedNativeAd.getHeadline());
                                        }
                                    }
                )
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        onAdListener.onFailed();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void hideAppIcon(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, com.example.capture.screens.MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
