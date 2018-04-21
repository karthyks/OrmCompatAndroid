package com.github.karthyks.sample.localstore;


import com.github.karthyks.ormcompat.provider.StoreContentHelper;
import com.github.karthyks.sample.datamodels.SampleData;

public class SampleDataContentHelper extends StoreContentHelper {

  public SampleDataContentHelper(String authority, String tableName) {
    super(authority, tableName);
  }

  @Override
  public void buildAllColumns() {
    this.addColumn(SampleData.COLUMN_MOBILE, Type.TEXT, Constraint.UNIQUE)
        .addColumn(SampleData.COLUMN_ADDRESS, Type.TEXT)
        .addColumn(SampleData.COLUMN_NAME, Type.TEXT, Constraint.NOT_NULL)
        .build();
  }
}
