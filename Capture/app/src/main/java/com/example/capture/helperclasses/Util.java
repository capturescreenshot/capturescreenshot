package com.example.capture.helperclasses;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.capture.services.ScreenshotService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, ScreenshotService.class);
        JobInfo.Builder builder = new JobInfo.Builder(123, serviceComponent);
        builder.setPeriodic(60 * 60 * 1000);
        builder.setPersisted(true); //restart on reboot

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        int resultCode = jobScheduler.schedule(builder.build());

       if(resultCode == JobScheduler.RESULT_SUCCESS){
           Log.d(Constants.TAG,"Job Scheduled");
       }
    }

    public static void hasActiveInternetConnection(final InternetConnectionListener internetConnectionListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();

                    if((urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0)){
                        //  Constants.printLog("Has Active Connection");
                        internetConnectionListener.onSuccess();
                    }else{
                        //  Constants.printLog("Has No Active Connection");
                        internetConnectionListener.onFailed("Limited");
                    }

                } catch (IOException e) {
                    internetConnectionListener.onFailed("Limited");
                   // Log.d(Constants.TAG,e.getMessage());
                }
            }
        }).start();
    }
    public interface InternetConnectionListener{
        void onSuccess();
        void onFailed(String status);
    }

}