package com.github.karthyks.sample.main;


import com.github.karthyks.sample.localstore.SampleDataLoader;

public interface IMainView {
  MainActivity getHostingActivity();

  void loadSampleData(SampleDataLoader dataLoader);
}
