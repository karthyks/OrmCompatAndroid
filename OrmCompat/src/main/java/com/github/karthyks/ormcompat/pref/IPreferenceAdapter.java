package com.github.karthyks.ormcompat.pref;


import android.content.SharedPreferences;

public interface IPreferenceAdapter<T> {
  T get(String key, SharedPreferences sharedPreferences);

  /** This method should not call commit or apply on Shared Preferences */
  void write(String key, T value, SharedPreferences.Editor editor);

  IPreferenceAdapter<Boolean> BOOLEAN_ADAPTER = new IPreferenceAdapter<Boolean>() {
    @Override public Boolean get(String key, SharedPreferences sharedPreferences) {
      return sharedPreferences.getBoolean(key, false);
    }

    @Override public void write(String key, Boolean value, SharedPreferences.Editor editor) {
      editor.putBoolean(key, value);
    }
  };

  IPreferenceAdapter<String> STRING_ADAPTER = new IPreferenceAdapter<String>() {
    @Override public String get(String key, SharedPreferences sharedPreferences) {
      return sharedPreferences.getString(key, "none");
    }

    @Override public void write(String key, String value, SharedPreferences.Editor editor) {
      editor.putString(key, value);
    }
  };

  IPreferenceAdapter<Float> FLOAT_ADAPTER = new IPreferenceAdapter<Float>() {
    @Override public Float get(String key, SharedPreferences sharedPreferences) {
      return sharedPreferences.getFloat(key, 0f);
    }

    @Override public void write(String key, Float value, SharedPreferences.Editor editor) {
      editor.putFloat(key, value);
    }
  };

  IPreferenceAdapter<Long> LONG_ADAPTER = new IPreferenceAdapter<Long>() {
    @Override public Long get(String key, SharedPreferences sharedPreferences) {
      return sharedPreferences.getLong(key, 0L);
    }

    @Override public void write(String key, Long value, SharedPreferences.Editor editor) {
      editor.putLong(key, value);
    }
  };

  IPreferenceAdapter<Integer> INT_ADAPTER = new IPreferenceAdapter<Integer>() {
    @Override public Integer get(String key, SharedPreferences sharedPreferences) {
      return sharedPreferences.getInt(key, 0);
    }

    @Override public void write(String key, Integer value, SharedPreferences.Editor editor) {
      editor.putInt(key, value);
    }
  };
}
