package com.github.karthyks.sample.localstore;


import android.content.ContentResolver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.github.karthyks.ormcompat.dao.Dao;
import com.github.karthyks.ormcompat.dao.DataModel;
import com.github.karthyks.ormcompat.loader.DataLoader;
import com.github.karthyks.sample.datamodels.SampleData;

public class SampleDataLoader extends DataLoader<SampleDataLoader.SampleDataDao> {

  public SampleDataLoader(AppCompatActivity activity, SampleDataDao dao,
                          Class<? extends DataModel> dataModel) {
    super(SampleData.TABLE_NAME, activity, dao, dataModel);
  }

  public static class SampleDataDao extends Dao<SampleData> {

    public SampleDataDao(ContentResolver contentResolver, Uri uri) {
      super(contentResolver, uri);
    }

    /**
     * Override any crud operations if necessary.
     */
  }
}
