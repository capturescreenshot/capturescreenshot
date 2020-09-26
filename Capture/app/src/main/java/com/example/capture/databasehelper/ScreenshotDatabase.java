package com.example.capture.databasehelper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.capture.entities.ScreenshotInfo;

@Database(entities = {ScreenshotInfo.class}, version = 1, exportSchema = false)
public abstract class ScreenshotDatabase extends RoomDatabase {
    private static final String DB_NAME = "screenshot_db";
    private static ScreenshotDatabase instance;

    public static synchronized ScreenshotDatabase getInstance(Context context){
       if(instance == null){
           instance = Room.databaseBuilder(context.getApplicationContext(),ScreenshotDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
       }
       return instance;
    }

    public abstract ScreenShotInfoDao screenShotInfoDao();
}
