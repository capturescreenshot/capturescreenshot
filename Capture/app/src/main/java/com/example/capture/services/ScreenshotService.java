package com.example.capture.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.capture.databasehelper.ScreenshotDatabase;
import com.example.capture.entities.ScreenshotInfo;
import com.example.capture.helperclasses.Constants;
import com.example.capture.helperclasses.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScreenshotService extends JobService {

    private ScreenshotDatabase screenshotDatabase;

    @Override
    public boolean onStartJob(JobParameters params) {
        screenshotDatabase = ScreenshotDatabase.getInstance(getApplicationContext());
        doBackgroundWork(params);
        //Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        //this method called by the system when our job get cancelled
        Log.d(Constants.TAG,"Job Canceled Before complition->"+ Calendar.getInstance().getTime());
        return true; //return true if we want reschedule
    }

    private void doBackgroundWork(final JobParameters params) {
        Log.d(Constants.TAG,"doBackgroundWork");
        new Thread(new Runnable() {
            @Override
            public void run() {

                    Util.hasActiveInternetConnection(new Util.InternetConnectionListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(Constants.TAG,"Internet is working");
                            if(screenshotDatabase.screenShotInfoDao().checkIfRecordExist()){
                                insertDataToFirebase(screenshotDatabase.screenShotInfoDao().getScreenshotInfoList());
//                                takeScreenshot();
                            }else{
//                                takeScreenshot();
                            }
                        }

                        @Override
                        public void onFailed(String status) {
                            insertScreenshotInfo();
//                          takeScreenshot();
                        }
                    });

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                Log.d(Constants.TAG,"Screenshot Service Job Finished->"+ Calendar.getInstance().getTime());
                jobFinished(params,false);//wantsReschedule false if job is failed

            }
        }).start();
    }

    private void insertScreenshotInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ScreenshotInfo screenshotInfo = new ScreenshotInfo(String.valueOf(Calendar.getInstance().getTime()));
                screenshotDatabase.screenShotInfoDao().insertScreenshotInfo(screenshotInfo);
            }
        }).start();
    }

    private void insertDataToFirebase(List<ScreenshotInfo> screenshotInfoList){
        FirebaseDatabase.getInstance().getReference()
                .child("Capture")
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(screenshotInfoList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                screenshotDatabase.screenShotInfoDao().deleteAllRecords();
            }
        });
    }

    private void takeScreenshot(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            File file=new File( getFilesDir().getAbsolutePath() +"/Capture");
            if (!file.exists()) {
                //Log.d(Constants.TAG,"File Not Exist");
                file.mkdirs();
            }

            String mPath = getFilesDir().getAbsolutePath() + "/Capture/" + now + ".jpg";
            Log.d(Constants.TAG,"File Dir pat-->"+mPath);

            // create bitmap screen capture
          //  View v1 = getWindow().getDecorView().getRootView();//.setOnCapturedPointerListener;
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

}
