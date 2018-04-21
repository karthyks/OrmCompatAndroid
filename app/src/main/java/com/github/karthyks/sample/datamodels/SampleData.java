package com.github.karthyks.sample.datamodels;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.github.karthyks.ormcompat.dao.DataModel;

public class SampleData extends DataModel {
  public static final String TABLE_NAME = "sampledata";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_ADDRESS = "address";
  public static final String COLUMN_MOBILE = "mobile";

  private String name;
  private String address;
  private String mobile;

  public SampleData() {
    super();
  }

  protected SampleData(Parcel in) {
    name = in.readString();
    address = in.readString();
    mobile = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(address);
    dest.writeString(mobile);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SampleData> CREATOR = new Creator<SampleData>() {
    @Override
    public SampleData createFromParcel(Parcel in) {
      return new SampleData(in);
    }

    @Override
    public SampleData[] newArray(int size) {
      return new SampleData[size];
    }
  };

  @Override
  public SampleData fromCursor(Cursor cursor) {
    SampleData sampleData = new SampleData();
    sampleData.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
    sampleData.address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
    sampleData.mobile = cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE));
    return sampleData;
  }

  @Override public String[] projection() {
    return new String[]{
        COLUMN_NAME, COLUMN_ADDRESS, COLUMN_MOBILE
    };
  }

  @Override
  public ContentValues toContentValues() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_NAME, name);
    contentValues.put(COLUMN_ADDRESS, address);
    contentValues.put(COLUMN_MOBILE, mobile);
    return contentValues;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getAddress() {
    return address;
  }

  public String getMobile() {
    return mobile;
  }
}
