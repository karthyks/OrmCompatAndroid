package com.github.karthyks.ormcompat.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.karthyks.ormcompat.R;

import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

  private List<String> createQueries;
  private List<String> dropQueries;

  public DbHelper(Context context, List<String> createQueries, List<String> dropQueries) {
    super(context, context.getResources().getString(R.string.database_name),
        null, context.getResources().getInteger(R.integer.database_version));
    this.createQueries = createQueries;
    this.dropQueries = dropQueries;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    for (String createQuery : createQueries) {
      db.execSQL(createQuery);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    for (String dropQuery : dropQueries) {
      db.execSQL(dropQuery);
    }
    onCreate(db);
  }
}
