package com.example.capture.databasehelper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.capture.entities.ScreenshotInfo;

import java.util.List;

@Dao
public interface ScreenShotInfoDao {

    @Query("Select * from screenshot")
    List<ScreenshotInfo> getScreenshotInfoList();

    @Insert
    void insertScreenshotInfo(ScreenshotInfo screenshotInfo);

    @Query("SELECT EXISTS(SELECT * FROM screenshot)")
    Boolean checkIfRecordExist();

    @Query(" DELETE FROM screenshot")
    void deleteAllRecords();

}
