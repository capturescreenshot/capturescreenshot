package com.example.capture.screens;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capture.R;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private Button hideButton;
    private UnifiedNativeAdView adView;
    private ImageView adImage;
    private ImageView adIcon;
    private TextView adBody;

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(permissions);
        bindViews();
        initialize();
    }


    @Override
    public void bindViews() {
        adView =  findViewById(R.id.adview);
        hideButton = findViewById(R.id.hide_button);
        adImage = adView.findViewById(R.id.ad_image);
        adIcon = adView.findViewById(R.id.ad_app_icon);
        adBody = adView.findViewById(R.id.ad_body);
    }

    @Override
    public void initialize() {
        mPresenter = new MainPresenter(MainActivity.this);
        mPresenter.loadAd(getBaseContext());

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startService(getBaseContext());
                mPresenter.hideApp(getBaseContext());
            }
        });

    }

    private void requestPermissions(String[] permissions) {
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.

                //initialize();

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "denied", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    @Override
    public void displayAd(Drawable adImageDrawable, Drawable adIconDrawable, String body, String headline) {
        adImage.setImageDrawable(adImageDrawable);
        adIcon.setImageDrawable(adIconDrawable);

        adBody.setText(body);
        adView.setBodyView(adBody);
        adView.setImageView(adImage);
    }
}