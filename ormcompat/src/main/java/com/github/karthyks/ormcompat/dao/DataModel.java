package com.github.karthyks.ormcompat.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

public abstract class DataModel implements Parcelable, Cloneable {

  public DataModel() {

  }

  public abstract DataModel fromCursor(Cursor cursor);

  public abstract String[] projection();

  public abstract ContentValues toContentValues();
}
