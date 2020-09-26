package com.example.capture.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "screenshot")
public class ScreenshotInfo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "file_name")
    private String fileName;

    public ScreenshotInfo(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    @Ignore
    public ScreenshotInfo(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
