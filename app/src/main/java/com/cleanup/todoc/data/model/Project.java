package com.cleanup.todoc.data.model;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "projects")
public class Project {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    private final String name;

    @ColorInt
    private final int color;

    public Project(@NonNull String name, @ColorInt int color) {
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }

    public void setId(long id) {
        this.id = id;
    }
}
