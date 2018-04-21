package com.github.karthyks.sample.main;


import com.github.karthyks.ormcompat.provider.StoreContentHelper;
import com.github.karthyks.sample.datamodels.SampleData;
import com.github.karthyks.sample.localstore.SampleDataLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPresenter implements IMainPresenter {

  private IMainView mainView;
  private SampleDataLoader.SampleDataDao dataDao;

  private ExecutorService threadPoolService = Executors.newFixedThreadPool(2);

  MainPresenter(IMainView mainView) {
    this.mainView = mainView;
  }

  @Override
  public void onViewReady() {
    dataDao = new SampleDataLoader.SampleDataDao(mainView.getHostingActivity().getContentResolver(),
        StoreContentHelper.getContentUri(SampleData.TABLE_NAME));
    SampleDataLoader dataLoader = new SampleDataLoader(mainView.getHostingActivity(), dataDao,
        SampleData.class);
    mainView.loadSampleData(dataLoader);
  }

  @Override
  public void addItem(String name, String address, String mobile) {
    final SampleData sampleData = new SampleData();
    sampleData.setName(name);
    sampleData.setAddress(address);
    sampleData.setMobile(mobile);
    threadPoolService.submit(new Runnable() {
      @Override public void run() {
        dataDao.insertOne(sampleData);
        dataDao.apply();
      }
    });
  }
}
