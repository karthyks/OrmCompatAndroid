package com.github.karthyks.ormcompat.dao;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.CallSuper;

import com.github.karthyks.ormcompat.provider.StoreContentHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Dao<T extends DataModel> {
  private static final String TAG = Dao.class.getSimpleName();

  private String authority;
  private ContentResolver contentResolver;
  private Uri uri;
  private ArrayList<ContentProviderOperation> batch;
  private String selection;
  private String[] selectionArgs;
  private Cursor cursor;

  public Dao(ContentResolver contentResolver, Uri uri) {
    this.authority = StoreContentHelper.getAuthority();
    this.contentResolver = contentResolver;
    this.uri = uri;
  }

  public List<T> getFromCursor(Cursor cursor, Class<? extends DataModel> dataClass) {
    List<T> dataModels = new LinkedList<>();
    T dataModel = getInstanceOfClass(dataClass);
    if (dataModel == null) return dataModels;
    if (cursor == null || cursor.getCount() <= 0) {
      return new LinkedList<>();
    }
    cursor.moveToFirst();
    do {
      dataModels.add((T) dataModel.fromCursor(cursor));
    } while (cursor.moveToNext());
    return dataModels;
  }

  public List<T> findAll(Class<? extends DataModel> dataClass) {
    List<T> dataModels = new LinkedList<>();
    T dataModel = getInstanceOfClass(dataClass);
    if (dataModel == null) return dataModels;
    cursor = contentResolver.query(uri, dataModel.projection(), null, null, null);
    if (cursor == null || cursor.getCount() <= 0) {
      return new LinkedList<>();
    }
    cursor.moveToFirst();
    do {
      dataModels.add((T) dataModel.fromCursor(cursor));
    } while (cursor.moveToNext());
    cursor.close();
    return dataModels;
  }

  /**
   * @param dataClass  - Class for which the data has to be returned.
   * @param columnName - Sorted based on the column in ASC order.
   * @return
   */
  public List<T> findAllAndSortByColumn(Class<? extends DataModel> dataClass, String columnName,
                                        boolean asc) {
    List<T> dataModels = new LinkedList<>();
    T dataModel = getInstanceOfClass(dataClass);
    if (dataModel == null) return dataModels;
    String sort = asc ? " ASC" : " DESC";
    cursor = contentResolver.query(uri, dataModel.projection(), null, null, columnName + sort);
    if (cursor == null || cursor.getCount() <= 0) {
      return new LinkedList<>();
    }
    cursor.moveToFirst();
    do {
      dataModels.add((T) dataModel.fromCursor(cursor));
    } while (cursor.moveToNext());
    cursor.close();
    return dataModels;
  }

  /**
   * @param dataClass  DataModel.class to be returned
   * @param columnName columnName to search from
   * @param values     values to search with.
   * @return returns a list of dataModel which match the values in the column.
   */

  public List<T> findByColumnValues(Class<? extends DataModel> dataClass, String columnName,
                                    String... values) {
    selection = columnName + "=?";
    selectionArgs = values.clone();
    List<T> dataModels = new LinkedList<>();
    T dataModel = getInstanceOfClass(dataClass);
    if (dataModel == null) return dataModels;
    cursor = contentResolver.query(uri, dataModel.projection(), selection, selectionArgs, null);
    if (cursor == null || cursor.getCount() <= 0) {
      return null;
    }
    cursor.moveToFirst();
    do {
      dataModels.add((T) dataModel.fromCursor(cursor));
    } while (cursor.moveToNext());
    cursor.close();
    return dataModels;
  }

  /**
   * @param dataClass  DataModel.class to be returned
   * @param columnName column to search from
   * @param value      value to search with
   * @return the first dataModel which matches the value in the column.
   */

  public T findOneByColumnValue(Class<? extends DataModel> dataClass, String columnName,
                                String value) {
    selection = columnName + "=?";
    selectionArgs = new String[]{value};
    T dataModel = getInstanceOfClass(dataClass);
    if (dataModel == null) return null;
    cursor = contentResolver.query(uri, dataModel.projection(), selection, selectionArgs, null);
    if (cursor == null || cursor.getCount() <= 0) {
      return null;
    }
    cursor.moveToFirst();
    dataModel = (T) dataModel.fromCursor(cursor);
    cursor.close();
    return dataModel;
  }

  /**
   * Always call apply after all the insertion, update and deletion
   */

  public void insertOne(T object) {
    if (batch == null) batch = new ArrayList<>();
    batch.add(ContentProviderOperation.newInsert(uri)
        .withValues(object.toContentValues()).build());
  }

  /**
   * Always call apply after all the insertion, update and deletion
   */

  public void insertAll(List<T> objectList) {
    if (batch == null) batch = new ArrayList<>();
    for (T object : objectList) {
      batch.add(ContentProviderOperation.newInsert(uri)
          .withValues(object.toContentValues()).build());
    }
  }

  /**
   * Call super method in the beginning.
   *
   * @param columnName - Name of the column which should be searched upon
   * @param object     - Model object to update.
   * @param values     - Rows with the values in the column will be updated.
   *                   Always call apply after all the insertion, update and deletion
   */
  @CallSuper
  public void updateByColumnValues(String columnName, T object, String... values) {
    selection = columnName + "=?";
    selectionArgs = values.clone();
    batch.add(ContentProviderOperation.newUpdate(uri)
        .withSelection(selection, selectionArgs)
        .withValues(object.toContentValues()).build());
  }

  /**
   * Call super method in the beginning.
   *
   * @param columnName - Name of the column which should be searched upon
   * @param values     - Rows will be deleted which has the values in the column.
   *                   Always call apply after all the insertion, update and deletion
   */
  @CallSuper
  public void deleteByColumnValues(String columnName, String... values) {
    selection = columnName + "=?";
    selectionArgs = values.clone();
    batch.add(ContentProviderOperation.newDelete(uri)
        .withSelection(selection, selectionArgs).build());
  }

  public void deleteAll() {
    contentResolver.delete(uri, null, null);
  }

  /**
   * Always call apply after all the insertion, update and deletion
   */
  public void apply() {
    if (batch != null && batch.size() > 0) {
      try {
        contentResolver.applyBatch(authority, batch);
        batch.clear();
      } catch (RemoteException | OperationApplicationException e) {
        e.printStackTrace();
      }
    }
  }

  private T getInstanceOfClass(Class<? extends DataModel> cls) {
    try {
      return (T) cls.getConstructor().newInstance();
    } catch (Exception e) {
      return null;
    }
  }

  public String[] getProjection(Class<? extends DataModel> dataClass) {
    T dataModel = getInstanceOfClass(dataClass);
    if (dataModel != null) {
      return dataModel.projection();
    }
    return new String[]{};
  }
}
