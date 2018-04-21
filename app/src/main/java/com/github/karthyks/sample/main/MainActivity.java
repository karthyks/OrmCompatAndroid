package com.github.karthyks.sample.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.karthyks.ormcompat.loader.DataLoader;
import com.github.karthyks.sample.R;
import com.github.karthyks.sample.datamodels.SampleData;
import com.github.karthyks.sample.localstore.SampleDataLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DataLoader.DataCallback<SampleData>,
    View.OnClickListener, IMainView {

  private static final String TAG = MainActivity.class.getSimpleName();
  private SampleDataAdapter adapter;

  private IMainPresenter mainPresenter;
  private AddItemDialog addItemDialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerView rvSampleData = findViewById(R.id.rv_items);
    rvSampleData.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SampleDataAdapter(this);
    rvSampleData.setAdapter(adapter);
    Button btnAdd = findViewById(R.id.btn_add_item);
    btnAdd.setOnClickListener(this);
    mainPresenter = new MainPresenter(this);
    addItemDialog = new AddItemDialog(this, mainPresenter);
  }

  @Override protected void onResume() {
    super.onResume();
    mainPresenter.onViewReady();
  }

  /**
   * Initialize data loader with your custom request
   */

  @Override
  public void loadSampleData(SampleDataLoader dataLoader) {
    dataLoader.requestor().loadRequest(this);
  }

  @Override
  public void onDataAvailable(List<SampleData> dataModels) {
    Log.d(TAG, "onDataAvailable: " + dataModels.size());
    adapter.swapSampleData(dataModels);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_add_item:
        addItemDialog.show();
        break;
      default:
    }
  }

  @Override
  public MainActivity getHostingActivity() {
    return this;
  }
}