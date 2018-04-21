package com.github.karthyks.sample.main;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.karthyks.sample.databinding.LayoutRowItemBinding;
import com.github.karthyks.sample.datamodels.SampleData;

import java.util.LinkedList;
import java.util.List;

public class SampleDataAdapter extends RecyclerView.Adapter<
    SampleDataAdapter.SampleDataViewHolder> {

  private Context context;
  private List<SampleData> sampleData = new LinkedList<>();

  public SampleDataAdapter(Context context) {
    this.context = context;
  }

  @NonNull
  @Override
  public SampleDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    LayoutRowItemBinding itemBinding = LayoutRowItemBinding.inflate(inflater, parent, false);
    return new SampleDataViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull SampleDataViewHolder holder, int position) {
    holder.bind(sampleData.get(position));
  }

  @Override
  public int getItemCount() {
    return sampleData.size();
  }

  public void swapSampleData(List<SampleData> sampleData) {
    this.sampleData.clear();
    this.sampleData.addAll(sampleData);
    notifyDataSetChanged();
  }

  public class SampleDataViewHolder extends RecyclerView.ViewHolder {

    private LayoutRowItemBinding itemBinding;

    public SampleDataViewHolder(LayoutRowItemBinding itemBinding) {
      super(itemBinding.getRoot());
      this.itemBinding = itemBinding;
    }

    public void bind(SampleData sampleData) {
      itemBinding.setSampleData(sampleData);
      itemBinding.executePendingBindings();
    }
  }
}