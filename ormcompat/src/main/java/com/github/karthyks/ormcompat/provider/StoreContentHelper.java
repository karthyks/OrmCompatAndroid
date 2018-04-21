package com.github.karthyks.ormcompat.provider;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.LinkedList;
import java.util.List;

import static android.provider.BaseColumns._ID;

public abstract class StoreContentHelper {
  public static final String BASE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd"
      + ".local.store";
  public static final String BASE_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd"
      + ".local.store";

  public static String AUTHORITY = "";
  private static final String COMMA_DELIMITER = ",";

  private static int TOTAL_ROUTE = 0;

  protected static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

  static final List<String> CREATE_TABLE_QUERIES = new LinkedList<>();
  static final List<String> DROP_TABLE_QUERIES = new LinkedList<>();

  public static final List<StoreContentHelper> storeContentHelpers = new LinkedList<>();

  private SQLiteOpenHelper databaseHelper;
  private Context context;

  private String tableName;
  private StringBuilder stringBuilder;
  private int route;
  private String contentItemType;
  private String contentType;

  public StoreContentHelper(String authority, String tableName) {
    this.tableName = tableName;
    this.contentType = StoreContentHelper.BASE_CONTENT_TYPE + tableName;
    this.contentItemType = StoreContentHelper.BASE_CONTENT_ITEM_TYPE + tableName + "s";
    this.route = TOTAL_ROUTE++;
    URI_MATCHER.addURI(authority, tableName + "/*", route);
    stringBuilder = new StringBuilder("CREATE TABLE " + tableName + " (" + _ID
        + Type.INTEGER + " PRIMARY KEY");
    buildAllColumns();
  }

  public void openConnection(Context context, SQLiteOpenHelper databaseHelper) {
    this.context = context;
    this.databaseHelper = databaseHelper;
  }

  public Context getContext() {
    return context;
  }

  public int[] getSupportedRoutes() {
    return new int[]{route};
  }

  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) {
    int match = URI_MATCHER.match(uri);
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    if (match == route) {
      Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null,
          null, sortOrder);
      cursor.setNotificationUri(getContext().getContentResolver(), uri);
      return cursor;
    } else {
      throw new UnsupportedOperationException("Unsupported URI : " + uri);
    }
  }

  public String getType(Uri uri) {
    final int match = URI_MATCHER.match(uri);
    if (match == route) {
      return contentItemType;
    } else {
      throw new UnsupportedOperationException("Unsupported URI: " + uri);
    }
  }

  public Uri insert(Uri uri, ContentValues contentValues) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    int match = URI_MATCHER.match(uri);
    if (match == route) {
      db.insertOrThrow(tableName, null, contentValues);
    } else {
      throw new UnsupportedOperationException("Unsupported URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null, false);
    return uri;
  }

  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    int match = URI_MATCHER.match(uri);
    int result;
    if (match == route) {
      result = db.delete(tableName, selection, selectionArgs);
    } else {
      throw new UnsupportedOperationException("Unsupported URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null, false);
    return result;
  }

  public int update(Uri uri, ContentValues contentValues, String selection,
                    String[] selectionArgs) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    int match = URI_MATCHER.match(uri);
    int result;
    if (match == route) {
      result = db.update(tableName, contentValues, selection, selectionArgs);
    } else {
      throw new UnsupportedOperationException("Unsupported URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null, false);
    return result;
  }

  public abstract void buildAllColumns();

  public void build() {
    stringBuilder.append(" )");
    CREATE_TABLE_QUERIES.add(stringBuilder.toString());
    DROP_TABLE_QUERIES.add("DROP TABLE IF EXISTS " + tableName);
  }

  public StoreContentHelper addColumn(String key, String type, String... constraint) {
    stringBuilder.append(COMMA_DELIMITER).append(key).append(type);
    if (constraint != null && constraint.length > 0) {
      for (String c : constraint) {
        stringBuilder.append(c);
      }
    }
    return this;
  }

  protected static class Type {
    public static final String TEXT = " TEXT";
    public static final String INTEGER = " INTEGER";
  }

  protected static class Constraint {
    public static final String NOT_NULL = " NOT NULL";
    public static final String UNIQUE = " UNIQUE";
  }

  public static Uri getContentUri(String tableName) {
    return Uri.parse("content://" + getAuthority()).buildUpon().appendPath(tableName)
        .appendPath("all").build();
  }

  public static String getAuthority() {
    return AUTHORITY + ".local.store";
  }
}
