package com.github.karthyks.sample;


import android.app.Application;

import com.github.karthyks.ormcompat.loader.DataLoader;
import com.github.karthyks.ormcompat.provider.StoreContentHelper;
import com.github.karthyks.sample.datamodels.SampleData;
import com.github.karthyks.sample.localstore.SampleDataContentHelper;

public class SampleApplication extends Application {
  static {
    StoreContentHelper.AUTHORITY = "com.github.karthyks.sample";
    DataLoader.TABLE_LIST.add(SampleData.TABLE_NAME);
    StoreContentHelper.storeContentHelpers.add(new SampleDataContentHelper(
        StoreContentHelper.getAuthority(), SampleData.TABLE_NAME));
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
