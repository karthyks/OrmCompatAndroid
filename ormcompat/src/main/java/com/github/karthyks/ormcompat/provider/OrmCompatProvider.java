package com.github.karthyks.ormcompat.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashSet;
import java.util.Set;

import static com.github.karthyks.ormcompat.provider.StoreContentHelper.URI_MATCHER;
import static com.github.karthyks.ormcompat.provider.StoreContentHelper.storeContentHelpers;

public class OrmCompatProvider extends ContentProvider {
  private static final String TAG = OrmCompatProvider.class.getSimpleName();

  public DbHelper dbHelper;
  private SparseArray<StoreContentHelper> sContentHelpers = new SparseArray<>();

  private void buildContentHelpers() {
    Set<Integer> routeCheck = new HashSet<>();
    for (StoreContentHelper contentHelper : storeContentHelpers) {
      for (int route : contentHelper.getSupportedRoutes()) {
        if (!routeCheck.add(route)) {
          throw new IllegalStateException("Found Content helper with duplicate routes "
              + route);
        }
        sContentHelpers.put(route, contentHelper);
      }
    }
  }

  @Override
  public boolean onCreate() {
    if (getContext() == null) {
      Log.d(TAG, "onCreate: Failed");
      return false;
    }
    String authority = StoreContentHelper.getAuthority();
    Log.d(TAG, "onCreate: " + authority);
    buildContentHelpers();
    dbHelper = new DbHelper(getContext(), StoreContentHelper.CREATE_TABLE_QUERIES,
        StoreContentHelper.DROP_TABLE_QUERIES);
    return false;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                      @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    int match = URI_MATCHER.match(uri);
    StoreContentHelper helper = sContentHelpers.get(match);
    helper.openConnection(getContext(), dbHelper);
    return helper.query(uri, projection, selection, selectionArgs, sortOrder);
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    final int match = URI_MATCHER.match(uri);
    StoreContentHelper helper = sContentHelpers.get(match);
    helper.openConnection(getContext(), dbHelper);
    return helper.getType(uri);
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    final int match = URI_MATCHER.match(uri);
    StoreContentHelper helper = sContentHelpers.get(match);
    helper.openConnection(getContext(), dbHelper);
    return helper.insert(uri, values);
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection,
                    @Nullable String[] selectionArgs) {
    int match = URI_MATCHER.match(uri);
    StoreContentHelper helper = sContentHelpers.get(match);
    helper.openConnection(getContext(), dbHelper);
    return helper.delete(uri, selection, selectionArgs);
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values,
                    @Nullable String selection, @Nullable String[] selectionArgs) {
    int match = URI_MATCHER.match(uri);
    StoreContentHelper contentHelper = sContentHelpers.get(match);
    contentHelper.openConnection(getContext(), dbHelper);
    return contentHelper.update(uri, values, selection, selectionArgs);
  }
}
