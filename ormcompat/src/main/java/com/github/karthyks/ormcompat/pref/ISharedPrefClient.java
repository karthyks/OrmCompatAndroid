package com.github.karthyks.ormcompat.pref;


import android.support.annotation.CheckResult;

public interface ISharedPrefClient extends IPrefHolder {
  /**
   * Clears all data from the shared preferences
   * of name {@link #getPreferenceName()} that is used by this instance.
   */
  void clearAll();

  /** @return Preference name used for this instance. */
  @CheckResult String getPreferenceName();
}
