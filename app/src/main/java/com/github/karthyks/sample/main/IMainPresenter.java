package com.github.karthyks.sample.main;


public interface IMainPresenter {
  void onViewReady();

  void addItem(String name, String address, String mobile);
}
