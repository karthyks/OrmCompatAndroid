package com.github.karthyks.ormcompat.loader;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.karthyks.ormcompat.dao.Dao;
import com.github.karthyks.ormcompat.dao.DataModel;
import com.github.karthyks.ormcompat.provider.StoreContentHelper;

import java.util.LinkedList;
import java.util.List;

public abstract class DataLoader<D extends Dao>
    implements LoaderCallbacks<Cursor> {
  private static final String TAG = DataLoader.class.getSimpleName();
  public static final List<String> TABLE_LIST = new LinkedList<>();

  private String tableName;
  private AppCompatActivity activity;
  private LoaderManager loaderManager;
  private D dao;
  private Class<? extends DataModel> dataModelClass;
  private DataCallback dataCallback;

  public DataLoader(String tableName, AppCompatActivity activity, D dao,
                    Class<? extends DataModel> dataModelClass) {
    this.tableName = tableName;
    this.activity = activity;
    this.loaderManager = activity.getSupportLoaderManager();
    this.dao = dao;
    this.dataModelClass = dataModelClass;
  }

  void loadRequest(CursorRequest cursorRequest, DataCallback dataCallback) {
    this.dataCallback = dataCallback;
    Bundle args = cursorRequest.toArgs();
    loaderManager.initLoader(TABLE_LIST.indexOf(tableName), args, this);
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(activity, StoreContentHelper.getContentUri(TABLE_LIST.get(id)),
        dao.getProjection(dataModelClass),
        args != null ? CursorRequest.getSelection(args) : null,
        args != null ? CursorRequest.getSelectionArgs(args) : null,
        args != null ? CursorRequest.getSortOrder(args) : null);
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    dataCallback.onDataAvailable(dao.getFromCursor(data, dataModelClass));
  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {

  }

  public RequestBuilder requestor() {
    return new RequestBuilder(this);
  }

  public static class RequestBuilder {
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private DataLoader loader;

    public RequestBuilder(DataLoader loader) {
      this.loader = loader;
    }

    public RequestBuilder select(String selection, String[] selectionArgs) {
      this.selection = selection;
      this.selectionArgs = selectionArgs;
      return this;
    }

    public RequestBuilder sortOrder(String sortOrder) {
      this.sortOrder = sortOrder;
      return this;
    }

    public void loadRequest(DataCallback dataCallback) {
      CursorRequest cursorRequest = new CursorRequest(selection, selectionArgs, sortOrder);
      loader.loadRequest(cursorRequest, dataCallback);
    }
  }

  static class CursorRequest {
    static final String SELECTION = "selection";
    static final String SELECTION_ARGS = "selection_args";
    static final String SORT_ORDER = "sort_order";

    String selection;
    String[] selectionArgs;
    String sortOrder;

    CursorRequest(String selection, String[] selectionArgs, String sortOrder) {
      this.selection = selection;
      this.selectionArgs = selectionArgs;
      this.sortOrder = sortOrder;
    }

    static String getSelection(Bundle bundle) {
      return bundle.getString(SELECTION, null);
    }

    static String[] getSelectionArgs(Bundle bundle) {
      return bundle.getStringArray(SELECTION_ARGS);
    }

    static String getSortOrder(Bundle bundle) {
      return bundle.getString(SORT_ORDER, null);
    }

    Bundle toArgs() {
      Bundle args = new Bundle();
      args.putString(SELECTION, selection);
      args.putStringArray(SELECTION_ARGS, selectionArgs);
      args.putString(SORT_ORDER, sortOrder);
      return args;
    }
  }

  public interface DataCallback<R extends DataModel> {
    void onDataAvailable(List<R> dataModels);
  }
}
